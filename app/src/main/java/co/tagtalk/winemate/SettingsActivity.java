package co.tagtalk.winemate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import co.tagtalk.winemate.thriftfiles.MyProfile;
import co.tagtalk.winemate.thriftfiles.WineMateServices;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class SettingsActivity extends AppCompatActivity {

    private Integer userId;
    private CheckBox privateAccountCheckbox;
    private Boolean isHideProfileToStranger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);

        if (sharedPrefs.getInt(Configs.USER_ID, 0) != 0 && sharedPrefs.getBoolean(Configs.LOGIN_STATUS, false)) {
            userId = sharedPrefs.getInt(Configs.USER_ID, 0);
        } else {
            userId = Configs.userId;
        }

        Button myBottlesButton = (Button) findViewById(R.id.settings_bottle);
        Button myFriendListButton = (Button) findViewById(R.id.settings_group);
        Button homeButton = (Button) findViewById(R.id.settings_home);
        privateAccountCheckbox = (CheckBox) findViewById(R.id.settings_private_account_checkbox);

        if (myBottlesButton != null) {
            myBottlesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClassName("co.tagtalk.winemate",
                            "co.tagtalk.winemate.MyBottlesActivity");
                    startActivity(intent);
                }
            });
        }

        if (myFriendListButton != null) {
            myFriendListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClassName("co.tagtalk.winemate",
                            "co.tagtalk.winemate.FriendListActivity");
                    startActivity(intent);
                }
            });
        }

        if (homeButton != null) {
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClassName("co.tagtalk.winemate",
                            "co.tagtalk.winemate.NewsFeedActivity");
                    startActivity(intent);
                }
            });
        }

        if (privateAccountCheckbox != null) {
            privateAccountCheckbox.setEnabled(false);
            final SettingsActivity.GetMyProfileTask getMyProfileTask = new SettingsActivity.GetMyProfileTask();
            getMyProfileTask.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wine_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.log_out:
                Intent intentLogin = new Intent();
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                if (sharedPrefs.getInt(Configs.USER_ID, 0) != 0 && sharedPrefs.getBoolean(Configs.LOGIN_STATUS, false)) {
                    sharedPrefs.edit().clear().apply();
                }
                Configs.userId = 0;
                intentLogin.setClassName("co.tagtalk.winemate",
                        "co.tagtalk.winemate.LoginActivity");
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentLogin);
                finish();
                break;
            case R.id.my_profile:
                Intent intentMyProfile = new Intent();
                intentMyProfile.setClassName("co.tagtalk.winemate",
                        "co.tagtalk.winemate.UserProfileActivity");
                intentMyProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentMyProfile);
                break;
            case R.id.help_desk:
                Intent intentHelpDesk = new Intent();
                intentHelpDesk.setClassName("co.tagtalk.winemate",
                        "co.tagtalk.winemate.HelpDeskActivity");
                intentHelpDesk.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHelpDesk);
                break;
            case R.id.action_settings:
                Intent intentSettings = new Intent();
                intentSettings.setClassName("co.tagtalk.winemate",
                        "co.tagtalk.winemate.SettingsActivity");
                intentSettings.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentSettings);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetMyProfileTask extends AsyncTask<Void, Void, MyProfile> {
        @Override
        protected MyProfile doInBackground(Void... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            MyProfile myProfile = new MyProfile();

            try{
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);
                myProfile = client.getMyProfile(userId, userId);
            } catch (TException x) {
                x.printStackTrace();
            }
            transport.close();

            return myProfile;
        }

        @Override
        protected void onPostExecute(MyProfile myProfile) {
            isHideProfileToStranger = myProfile.isHideProfileToStranger();
            privateAccountCheckbox.setChecked(isHideProfileToStranger);

            privateAccountCheckbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                    if (isChecked == isHideProfileToStranger) {
                        // Current state matches desired state. No need to make service call.
                        return;
                    }
                    privateAccountCheckbox.setEnabled(false);
                    final SettingsActivity.TogglePrivacySettingTask togglePrivacySettingTask = new SettingsActivity.TogglePrivacySettingTask();
                    togglePrivacySettingTask.execute(isChecked);
                }
            });

            privateAccountCheckbox.setEnabled(true);
        }
    }

    private class TogglePrivacySettingTask extends AsyncTask<Boolean, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Boolean... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            Boolean success = false;

            try{
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);
                success = client.setPrivacy(userId, params[0]);
            } catch (TException x) {
                x.printStackTrace();
            }
            transport.close();

            // Service call succeed, update isHideProfileToStranger to new state.
            if (success) {
                isHideProfileToStranger = params[0];
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                // If failed to change the state, change checkbox back to the actual state.
                privateAccountCheckbox.setChecked(isHideProfileToStranger);
                Toast.makeText(SettingsActivity.this, getString(R.string.update_privacy_setting_failed_text), Toast.LENGTH_SHORT).show();
            }
            privateAccountCheckbox.setEnabled(true);
        }
    }
}
