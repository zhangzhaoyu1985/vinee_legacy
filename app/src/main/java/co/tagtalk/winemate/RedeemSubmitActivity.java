package co.tagtalk.winemate;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

import co.tagtalk.winemate.thriftfiles.Address;
import co.tagtalk.winemate.thriftfiles.RewardRedeemRequest;
import co.tagtalk.winemate.thriftfiles.RewardRedeemResponse;
import co.tagtalk.winemate.thriftfiles.RewardRedeemResponseCode;
import co.tagtalk.winemate.thriftfiles.RewardRedeemSingleItem;
import co.tagtalk.winemate.thriftfiles.WineMateServices;

import static co.tagtalk.winemate.Configs.RANDOM_KEY_RANGE;

public class RedeemSubmitActivity extends AppCompatActivity {

    private EditText fullName;
    private EditText street;
    private EditText city;
    private EditText province;
    private EditText country;
    private EditText zipCode;
    private EditText phoneNumber;
    private EditText email;
    private Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_submit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Integer userId;

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(RedeemSubmitActivity.this);

        if (sharedPrefs.getInt(Configs.USER_ID, 0) != 0 && sharedPrefs.getBoolean(Configs.LOGIN_STATUS, false)) {
            userId = sharedPrefs.getInt(Configs.USER_ID, 0);
        } else {
            userId = Configs.userId;
        }

        @SuppressWarnings("unchecked")
        final ArrayList<RewardRedeemSingleItem> rewardRedeemList = (ArrayList<RewardRedeemSingleItem>)getIntent().getSerializableExtra("rewardRedeemList");

        fullName = (EditText) findViewById(R.id.redeem_submit_fullname);
        street = (EditText) findViewById(R.id.redeem_submit_street);
        city = (EditText) findViewById(R.id.redeem_submit_city);
        province = (EditText) findViewById(R.id.redeem_submit_province);
        country = (EditText) findViewById(R.id.redeem_submit_country);
        zipCode = (EditText) findViewById(R.id.redeem_submit_zipcode);
        phoneNumber = (EditText) findViewById(R.id.redeem_submit_phone_number);
        email = (EditText) findViewById(R.id.redeem_submit_email);
        submitButton = (Button) findViewById(R.id.redeem_submit_submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    Address address = new Address();
                    address.fullName    = fullName.getText().toString();
                    address.street      = street.getText().toString();
                    address.city        = city.getText().toString();
                    address.province    = province.getText().toString();
                    address.country     = country.getText().toString();
                    address.zipCode     = zipCode.getText().toString();
                    address.phoneNumber = phoneNumber.getText().toString();
                    address.email = email.getText().toString();

                    Random random = new Random();
                    String  randomKey = String.valueOf(random.nextInt(RANDOM_KEY_RANGE));
                    randomKey = String.valueOf(userId) + randomKey + fullName.getText().toString() + phoneNumber.getText().toString();
                    String trackingNumber = "";

                    try {
                        MessageDigest md = null;
                        md = MessageDigest.getInstance("MD5");
                        md.update(randomKey.getBytes());
                        byte[] key = md.digest();
                        trackingNumber = byteArrayToHexString(key);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    RewardRedeemRequest rewardRedeemRequest = new RewardRedeemRequest(userId, rewardRedeemList, address, trackingNumber);
                    final RewardRedeemTask rewardRedeemTask = new RewardRedeemTask(RedeemSubmitActivity.this);
                    rewardRedeemTask.execute(rewardRedeemRequest);

                } else {
                    Toast.makeText(RedeemSubmitActivity.this, R.string.redeem_submit_activity_incomplete_info, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean isInputValid() {
        return !(fullName == null || street == null || city == null || province == null || country == null || zipCode == null || phoneNumber == null || email == null)
                && !(fullName.length() == 0 || street.length() == 0 || city.length() == 0 || province.length() == 0 || country.length() == 0|| zipCode.length() == 0 || phoneNumber.length() == 0);
    }

    private String byteArrayToHexString(byte[] inputArray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String hexString = "";

        if (inputArray == null) {
            return hexString;
        }

        for (j = 0; j < inputArray.length; ++j) {
            in = (int) inputArray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            hexString += hex[i];
            i = in & 0x0f;
            hexString += hex[i];
        }
        return hexString;
    }

    public class RewardRedeemTask extends AsyncTask<RewardRedeemRequest, Void, RewardRedeemResponse> {

        private Activity activity;

        public RewardRedeemTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected RewardRedeemResponse doInBackground(RewardRedeemRequest... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            RewardRedeemResponse rewardRedeemResponse = new RewardRedeemResponse();

            try {

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                rewardRedeemResponse = client.rewardRedeem(params[0]);


            } catch (TException x) {
                x.printStackTrace();
            }
            transport.close();

            return rewardRedeemResponse;
        }

        @Override
        protected void onPostExecute(final RewardRedeemResponse rewardRedeemResponse) {
            if (Configs.DEBUG_MODE) {
                Log.v("ZZZ WineryInfo", "rewardRedeemResponse: " + rewardRedeemResponse);
            }

            if (rewardRedeemResponse.resp_code != RewardRedeemResponseCode.FAILED) {
                Toast.makeText(RedeemSubmitActivity.this, R.string.redeem_submit_activity_submit_success, Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }
}
