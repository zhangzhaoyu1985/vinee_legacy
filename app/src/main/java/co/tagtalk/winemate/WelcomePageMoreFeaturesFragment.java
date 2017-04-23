package co.tagtalk.winemate;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class WelcomePageMoreFeaturesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    Activity activity;
    Button signUpButton;
    TextView loginText;

    public WelcomePageMoreFeaturesFragment() {}

    public static WelcomePageMoreFeaturesFragment newInstance() {
        WelcomePageMoreFeaturesFragment fragment = new WelcomePageMoreFeaturesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome_page_more_features, container, false);
        signUpButton = (Button) view.findViewById(R.id.welcome_activity_sign_up);
        loginText = (TextView) view.findViewById(R.id.welcome_activity_login);
        loginText.setPaintFlags(loginText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (signUpButton != null) {
            Log.d("More Feature Fragment","setup signUpButton listenr");
            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((OnFragmentInteractionListener)getActivity()).onSignUpListener();
                }
            });
        }
        if (loginText != null) {
            Log.d("More Feature Fragment","setup loginText listenr");
            loginText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((OnFragmentInteractionListener)getActivity()).onLoginListener();
                }
            });
        }
    }

    public interface OnFragmentInteractionListener {
        void onSignUpListener();
        void onLoginListener();
    }
}
