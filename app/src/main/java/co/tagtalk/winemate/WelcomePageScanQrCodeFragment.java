package co.tagtalk.winemate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WelcomePageScanQrCodeFragment extends Fragment {

    public WelcomePageScanQrCodeFragment() {}

    public static WelcomePageScanQrCodeFragment newInstance() {
        WelcomePageScanQrCodeFragment fragment = new WelcomePageScanQrCodeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.welcome_page_scan_qr_code, container, false);
    }
}
