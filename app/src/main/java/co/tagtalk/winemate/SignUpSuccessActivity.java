package co.tagtalk.winemate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUpSuccessActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_success);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent= getIntent();
        // Update activity title if provided.
        if (intent.hasExtra("updateSuccessTitle")) {
            TextView signup_activity_title = (TextView) findViewById(R.id.signup_activity_title);
            signup_activity_title.setText(intent.getStringExtra("updateSuccessTitle"));
        }
        // Update description text if provided.
        if (intent.hasExtra("updateSuccessText")) {
            TextView signUpDesc = (TextView) findViewById(R.id.signup_success_text);
            signUpDesc.setText(intent.getStringExtra("updateSuccessText"));
        }

        Button signUpSuccessLoginButton;
        signUpSuccessLoginButton = (Button) findViewById(R.id.signup_success_login_button);
        if (signUpSuccessLoginButton != null) {
            signUpSuccessLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClassName("co.tagtalk.winemate",
                            "co.tagtalk.winemate.LoginActivity");
                    startActivity(intent);
                }
            });
        }

    }

}
