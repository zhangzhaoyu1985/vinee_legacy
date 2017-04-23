package co.tagtalk.winemate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import co.tagtalk.winemate.thriftfiles.User;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button signUpButton;

        final EditText signUpUserName;
        final EditText signUpEmail;
        final EditText signUpPassword;
        final EditText signUpPasswordReenter;
        ToggleButton signUpShowHidePasswordSwitch;

        signUpButton     = (Button)findViewById(R.id.signup_signup_button);

        signUpUserName = (EditText)findViewById(R.id.signup_username);
        signUpEmail    = (EditText)findViewById(R.id.signup_email);
        signUpPassword = (EditText)findViewById(R.id.signup_password);
        signUpPasswordReenter = (EditText)findViewById(R.id.signup_password_reenter);

        signUpShowHidePasswordSwitch = (ToggleButton)findViewById(R.id.signup_show_hide_password_switch);

        if (signUpButton != null) {
            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isValidSignUp(signUpUserName, signUpEmail, signUpPassword, signUpPasswordReenter)) {
                        User user = new User();
                        user.userName = signUpUserName.getText().toString();
                        user.email    = signUpEmail.getText().toString();
                        user.password = signUpPassword.getText().toString();

                        final SignUpTask signUpTask = new SignUpTask(SignUpActivity.this);

                        signUpTask.execute(user);
                    }
                }
            });
        }

        if (signUpShowHidePasswordSwitch != null && signUpPassword != null && signUpPasswordReenter != null) {
            signUpShowHidePasswordSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {
                        signUpPassword.setTransformationMethod(null);
                        signUpPasswordReenter.setTransformationMethod(null);
                    } else {
                        signUpPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        signUpPasswordReenter.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                }
            });
        }
    }

    private boolean isValidSignUp(EditText userName, EditText email, EditText password, EditText passwordReenter) {
        if (userName == null || userName.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, R.string.user_name_no_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email == null || email.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, R.string.email_no_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password == null || password.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, R.string.password_no_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (passwordReenter == null || passwordReenter.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, R.string.reenter_password, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.getText().toString().equals(passwordReenter.getText().toString())) {
            Toast.makeText(SignUpActivity.this, R.string.password_not_match, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
