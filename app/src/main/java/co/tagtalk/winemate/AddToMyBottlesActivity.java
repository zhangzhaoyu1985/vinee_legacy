package co.tagtalk.winemate;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import co.tagtalk.winemate.thriftfiles.AddRewardPointsRequest;
import co.tagtalk.winemate.thriftfiles.BottleOpenInfo;
import co.tagtalk.winemate.thriftfiles.UserActions;
import co.tagtalk.winemate.thriftfiles.WineMateServices;

public class AddToMyBottlesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_my_bottles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent= getIntent();
        Integer wineId = intent.getIntExtra("wineId", 0);
        //String winePicURL = intent.getStringExtra("winePicURL");
        String tagId      = intent.getStringExtra("tagId");
        Integer userId    = intent.getIntExtra("userId", 0);
        String bottleOpenIdentifier = intent.getStringExtra("bottleOpenIdentifier");
        Integer rewardPoint = intent.getIntExtra("rewardPoint", 0);

        if (Configs.DEBUG_MODE) {
            Log.v("ZZZ bottleIdentifier: ", String.valueOf(bottleOpenIdentifier));
        }

        LocationService locationService = new LocationService(this);
        LocationInfo locationInfo = locationService.getCurrentLocationInfo();
        String detailedLocation = locationInfo.getLatitude() + ", " + locationInfo.getLongitude();

        TimeStamp timeStamp = new TimeStamp();

        BottleOpenInfo bottleOpenInfo = new BottleOpenInfo(tagId, wineId, userId, bottleOpenIdentifier, timeStamp.getCurrentDate(), timeStamp.getCurrentTime(), locationInfo.getCityName(), detailedLocation, locationInfo.getCountry());
        final AddToMyBottlesTask addToMyBottlesTask = new AddToMyBottlesTask(AddToMyBottlesActivity.this);
        addToMyBottlesTask.execute(bottleOpenInfo);

        AddRewardPointsRequest addRewardPointsRequest = new AddRewardPointsRequest(userId, UserActions.OpenedBottle, wineId);
        final RewardProgram.AddRewardPointsTask addRewardPointsTask = new RewardProgram.AddRewardPointsTask(this, rewardPoint);
        addRewardPointsTask.execute(addRewardPointsRequest);
    }

    public class AddToMyBottlesTask extends AsyncTask<BottleOpenInfo, Void, Boolean> {

        private Activity activity;

        public AddToMyBottlesTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(BottleOpenInfo... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            Boolean status = false;

            try {

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                status = client.openBottle(params[0]);


            } catch (TException x) {
                x.printStackTrace();
            }
            transport.close();

            return status;
        }

        @Override
        protected void onPostExecute(Boolean addToMyBottlesStatus) {

            if (addToMyBottlesStatus) {
                    Intent intent = new Intent();
                    intent.setClassName("co.tagtalk.winemate",
                            "co.tagtalk.winemate.MyBottlesActivity");
                    activity.startActivity(intent);
                    finish();
            } else {
                Toast.makeText(this.activity, "Failed to add to my bottles!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
