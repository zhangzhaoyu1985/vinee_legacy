package co.tagtalk.winemate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import co.tagtalk.winemate.thriftfiles.TagInfo;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static co.tagtalk.winemate.Constants.PERMISSIONS_REQUEST_CAMERA;

public class QRScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private Integer wineId;
    private Integer userId;
    private Integer rewardPoint;
    private String tagId;
    private TagInfo tagInfo;

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent= getIntent();
        wineId = intent.getIntExtra("wineId", 0);
        userId    = intent.getIntExtra("userId", 0);
        rewardPoint = intent.getIntExtra("rewardPoint", 0);

        tagId     = intent.getStringExtra("tagId");
        tagInfo   = (TagInfo)intent.getSerializableExtra("tagInfo");

        Toast.makeText(this, R.string.qrscanner_activity_hint, Toast.LENGTH_LONG).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            scanQRCode();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanQRCode();
            } else {
                Toast.makeText(this, R.string.qrscanner_activity_camera_request, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void onPause() {
        super.onPause();
        if (mScannerView != null) {
            mScannerView.stopCamera();
            finish();
        }
    }

    private void scanQRCode() {
        mScannerView = (ZXingScannerView) findViewById(R.id.qrscanner_scanner_view);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();

        if (tagInfo != null) {
            intent.putExtra("tagInfo", tagInfo);
            intent.setClassName("co.tagtalk.winemate",
                    "co.tagtalk.winemate.AuthenticationActivity");
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void handleResult(Result result) {

        String bottleOpenIdentifier = result.getText();

        Intent intent = new Intent();
        intent.putExtra("wineId", wineId);
        intent.putExtra("tagId", tagId);
        intent.putExtra("userId", userId);
        intent.putExtra("rewardPoint", rewardPoint);
        intent.putExtra("bottleOpenIdentifier", bottleOpenIdentifier);
        intent.setClassName("co.tagtalk.winemate",
                "co.tagtalk.winemate.AddToMyBottlesActivity");
        startActivity(intent);
        mScannerView.stopCamera();
        finish();
    }
}
