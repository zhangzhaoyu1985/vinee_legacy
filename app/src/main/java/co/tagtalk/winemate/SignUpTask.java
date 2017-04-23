package co.tagtalk.winemate;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import co.tagtalk.winemate.thriftfiles.RegistrationStatus;
import co.tagtalk.winemate.thriftfiles.User;
import co.tagtalk.winemate.thriftfiles.WineMateServices;

/**
 * Created by Zhaoyu on 2016/5/22.
 */
public class SignUpTask extends AsyncTask<User, Void, RegistrationStatus> {

    private Activity activity;
    private boolean gotException;

    public SignUpTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected RegistrationStatus doInBackground(User... params) {
        TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
        RegistrationStatus status = RegistrationStatus.REGISTRATION_DUPLICATE_USERNAME;

        try{

            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            WineMateServices.Client client = new WineMateServices.Client(protocol);

            status = client.registration(params[0]);

        } catch (TException x) {
            x.printStackTrace();
            gotException = true;
        }
        transport.close();

        return status;
    }

    @Override
    protected void onPostExecute(RegistrationStatus signUpStatus) {
        if (gotException) {
            Toast.makeText(this.activity, R.string.failed_to_do_operation, Toast.LENGTH_LONG).show();
            return;
        }

        if(signUpStatus == RegistrationStatus.REGISTRATION_SUCCESS) {
            Intent intent = new Intent();
            intent.setClassName("co.tagtalk.winemate",
                    "co.tagtalk.winemate.LoginActivity");

            activity.startActivity(intent);
            this.activity.finish();
        } else if (signUpStatus == RegistrationStatus.REGISTRATION_DUPLICATE_USERNAME) {
            Toast.makeText(this.activity, R.string.user_name_exists, Toast.LENGTH_SHORT).show();
        } else if (signUpStatus == RegistrationStatus.REGISTRATION_DUPLICATE_EMAIL) {
            Toast.makeText(this.activity, R.string.email_exists, Toast.LENGTH_SHORT).show();
        } else if (signUpStatus == RegistrationStatus.REGISTRATION_INVALID_INPUT) {
            Toast.makeText(this.activity, R.string.invalid_signup_input, Toast.LENGTH_SHORT).show();
        }
    }
}
