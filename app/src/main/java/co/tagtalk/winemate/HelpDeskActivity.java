package co.tagtalk.winemate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static co.tagtalk.winemate.Constants.REQUEST_CODE_SEND_EMAIL;

public class HelpDeskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_desk);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button myBottlesButton = (Button) findViewById(R.id.help_desk_bottle);
        Button myFriendListButton = (Button) findViewById(R.id.help_desk_group);
        Button homeButton = (Button) findViewById(R.id.help_desk_home);
        Button sendEmailButton = (Button) findViewById(R.id.contact_button);

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

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
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
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HelpDeskActivity.this);
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

    protected void sendEmail() {
        String[] to = new String[]{this.getString(R.string.tagtalk_email)};
        EditText nameEditText = (EditText) findViewById(R.id.contact_name);
        String name = nameEditText.getText().toString();
        EditText phoneEditText = (EditText) findViewById(R.id.contact_phone);
        String phone = phoneEditText.getText().toString();
        String subject = "Questions from " + name;
        if(phone != null && !phone.isEmpty()) {
            subject += " (" + phone + ")";
        }
        EditText messageEditText = (EditText) findViewById(R.id.contact_message);
        String message = messageEditText.getText().toString();
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivityForResult(Intent.createChooser(emailIntent, "Email"), REQUEST_CODE_SEND_EMAIL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SEND_EMAIL) {
            Intent intent = new Intent();
            intent.setClassName("co.tagtalk.winemate",
                    "co.tagtalk.winemate.NewsFeedActivity");
            this.startActivity(intent);
            finish();
        }
    }
}
