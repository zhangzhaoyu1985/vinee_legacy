package co.tagtalk.winemate;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.MifareUltralight;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.io.IOException;
import java.util.Locale;

import co.tagtalk.winemate.thriftfiles.CountryId;
import co.tagtalk.winemate.thriftfiles.TagInfo;
import co.tagtalk.winemate.thriftfiles.WineInfo;
import co.tagtalk.winemate.thriftfiles.WineMateServices;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;
import static android.view.View.GONE;
import static co.tagtalk.winemate.Configs.AUTHENTICATION_CODE_LENGTH;
import static co.tagtalk.winemate.Configs.AUTHENTICATION_CODE_PAGE_OFFSET;
import static co.tagtalk.winemate.Constants.PERMISSIONS_REQUEST_FINE_LOCATION;

public class AuthenticationActivity extends AppCompatActivity {

    private String LOG_TAG = AuthenticationActivity.class.getSimpleName();

    private Tag tag;
    private String tagID;
    private static Integer wineId;
    private String winePicURL;
    private static Integer userId;
    private byte[] tagPassword;
    private boolean badNetwork;

    private MifareUltralight ultralight;

    private ImageView winePic;
    private ImageView genuineSymbol;

    private TextView result;
    private TextView contactUs;
    private TextView wineName;
    private TextView wineYear;
    private TextView wineRegion;
    private TextView contactUsFake;
    private TextView wrongAuthCodeReminder;
    private TextView warningText;

    private Integer rewardPoint;
    private RatingBar wineRate;
    private Button viewMore;
    private Button wechatShare;
    private Button openBottle;

    private RelativeLayout authenticationRootLayout;
    private RelativeLayout progressBarLayout;

    final private Integer WINE_PIC_OFFSET_WIDTH = 430;
    final private Integer WINE_PIC_OFFSET_HEIGHT = 450;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.activity_authentication);

        authenticationRootLayout = (RelativeLayout) findViewById(R.id.authentication_content_root_relative_layout);
        progressBarLayout = (RelativeLayout) findViewById(R.id.authentication_progress_bar_relative_layout);
        warningText = (TextView) findViewById(R.id.authentication_warning);

        if (warningText != null) {
            warningText.setVisibility(GONE);
        }

        progressBarLayout.setVisibility(View.VISIBLE);
        authenticationRootLayout.setVisibility(GONE);

        // Set wine pic's height based on screen height.
        winePic = (ImageView) findViewById(R.id.authentication_wine_picture);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        android.view.ViewGroup.LayoutParams layoutParams = winePic.getLayoutParams();
        layoutParams.width = size.x - WINE_PIC_OFFSET_WIDTH;
        layoutParams.height = size.y - WINE_PIC_OFFSET_HEIGHT;
        winePic.setLayoutParams(layoutParams);
        winePic.setVisibility(View.VISIBLE);

        Button homeButton = (Button) findViewById(R.id.authentication_home);

        if (homeButton != null) {
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClassName("co.tagtalk.winemate",
                            "co.tagtalk.winemate.NewsFeedActivity");
                    startActivity(intent);
                    finish();
                }
            });
        }

        Intent intent = getIntent();
        TagInfo tagInfo = (TagInfo)intent.getSerializableExtra("tagInfo");

        if (tagInfo != null) {
            final AuthenticationTask authenticationTask = new AuthenticationTask(this);
            authenticationTask.execute(tagInfo, Configs.userId);
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            authenticate(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setClassName("co.tagtalk.winemate",
                "co.tagtalk.winemate.NewsFeedActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        authenticationRootLayout = (RelativeLayout) findViewById(R.id.authentication_content_root_relative_layout);
        progressBarLayout = (RelativeLayout) findViewById(R.id.authentication_progress_bar_relative_layout);

        if (warningText != null) {
            warningText.setVisibility(GONE);
        }

        progressBarLayout.setVisibility(View.VISIBLE);
        authenticationRootLayout.setVisibility(GONE);
        authenticate(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                authenticate(this.getIntent());
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(AuthenticationActivity.this);

                builder.setMessage(getString(R.string.location_permission_request_alert_message))
                        .setTitle(R.string.location_permission_request_alert_title);
                builder.setPositiveButton(R.string.permission_request_alert_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(AuthenticationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_FINE_LOCATION);
                    }
                });
                builder.setNegativeButton(R.string.permission_request_alert_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            }
        }
    }

    private void authenticate(Intent intent) {

        tagPassword = null;

        Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (parcelables != null && parcelables.length > 0 && intent.getType().equals(Configs.TAG_TYPE)) {
            tagID = byteArrayToHexString(tag.getId());
            ultralight = MifareUltralight.get(tag);
            final GetTagPasswordTask getTagPasswordTask = new GetTagPasswordTask(AuthenticationActivity.this);
            getTagPasswordTask.execute(tagID);

        } else {
            Log.v(LOG_TAG, "tagId = " + tagID);
            ultralight = null;
            Toast.makeText(this, R.string.authentication_activity_warning_no_ndef_messages, Toast.LENGTH_SHORT).show();
        }
    }

    public class AuthenticationTask extends AsyncTask<Object, Void, WineInfo> {

        private Activity activity;
        private TagInfo tagInfo;

        public AuthenticationTask(Activity activity) {
            this.activity = activity;

            wineName = (TextView) this.activity.findViewById(R.id.authentication_wine_name);
            wineYear = (TextView) this.activity.findViewById(R.id.authentication_wine_year);
            wineRegion = (TextView) this.activity.findViewById(R.id.authentication_wine_region);
            wineRate = (RatingBar) this.activity.findViewById(R.id.authentication_rate);
            openBottle = (Button) this.activity.findViewById(R.id.authentication_open_bottle);
            viewMore = (Button) findViewById(R.id.authentication_view_more_button);
            wechatShare = (Button) findViewById(R.id.authentication_share_button);
            genuineSymbol = (ImageView) findViewById(R.id.authentication_genuine_symbol);
            result = (TextView) findViewById(R.id.authentication_result_text);
            contactUs = (TextView) findViewById(R.id.authentication_contact_us);
        }

        @Override
        protected WineInfo doInBackground(Object... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            WineInfo wineInfo = null;
            tagInfo = (TagInfo) params[0];
            Integer userId = (Integer) params[1];

            try {

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                wineInfo = client.authentication(tagInfo, userId);
                badNetwork = false;

            } catch (TException x) {
                x.printStackTrace();
                AuthenticationActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        errorBadNetwork();
                    }
                });
            }
            transport.close();

            return wineInfo;
        }

        @Override
        protected void onPostExecute(final WineInfo wineInfo) {

            if (wineInfo == null) {
                if (!badNetwork) {
                    tagNoRecordProcessor();
                }
                return;
            }

            wechatShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("wineId", wineId);
                    intent.putExtra("url", wineInfo.wechatShareUrl);
                    intent.putExtra("getRewards", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setClassName("co.tagtalk.winemate",
                            "co.tagtalk.winemate.ShareDialogActivity");
                    startActivity(intent);
                }
            });

            if (!wineInfo.isGenuine) {
                tagNoRecordProcessor();
            } else {

                progressBarLayout.setVisibility(GONE);
                authenticationRootLayout.setVisibility(View.VISIBLE);

                wineName.setVisibility(View.VISIBLE);
                String wineNameString = getResources().getString(R.string.authentication_activity_the_wine) + " " + wineInfo.wineryName + " " + wineInfo.wineName;
                wineName.setText(wineNameString);

                wineYear.setVisibility(View.VISIBLE);
                String wineYearString = getResources().getString(R.string.authentication_activity_year) + " " + wineInfo.year;
                wineYear.setText(wineYearString);

                wineRegion.setVisibility(View.VISIBLE);
                String wineRegionString = getResources().getString(R.string.authentication_activity_region) + " " + wineInfo.regionName;
                wineRegion.setText(wineRegionString);


                wineId = wineInfo.wineId;
                winePicURL = wineInfo.winePicURL;
                final boolean isSealed = wineInfo.isSealed;

                rewardPoint = wineInfo.rewardPoint;

                viewMore.setVisibility(View.VISIBLE);

                if (viewMore != null) {

                    viewMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra("wineId", wineId);
                            intent.putExtra("winePicURL", winePicURL);
                            intent.putExtra("tagId", tagID);
                            intent.putExtra("isSealed", isSealed);

                            intent.setClassName("co.tagtalk.winemate",
                                    "co.tagtalk.winemate.WineInfoActivity");
                            startActivity(intent);
                            finish();
                        }
                    });
                }

                wineRate.setVisibility(View.VISIBLE);
                wineRate.setRating((float) wineInfo.wineRate);

                //Hide irrelevant UI elements
                if (contactUs != null) {
                    contactUs.setVisibility(GONE);
                }
                if (contactUsFake != null) {
                    contactUsFake.setVisibility(GONE);
                }
                if (wrongAuthCodeReminder != null) {
                    wrongAuthCodeReminder.setVisibility(GONE);
                }

                if (isSealed) {
                    Picasso.with(this.activity).load(wineInfo.winePicURL)
                            .error(R.drawable.placeholder)
                            .into(winePic);

                    if (Locale.getDefault().getLanguage().equals("zh")) {
                        genuineSymbol.setImageResource(R.drawable.genuine_symbol_true_zh);
                    } else {
                        genuineSymbol.setImageResource(R.drawable.genuine_symbol_true_en);
                    }

                    result.setText(getText(R.string.authentication_activity_result_genuine));
                    result.setTypeface(Typeface.DEFAULT_BOLD);
                    openBottle.setVisibility(View.VISIBLE);

                    if (openBottle != null) {

                        String openBottleButtonText = getText(R.string.authentication_activity_open_bottle_button_top) + "\n"
                                + getText(co.tagtalk.winemate.R.string.authentication_activity_open_bottle_button_middle_left) + " "
                                + String.valueOf(rewardPoint) + " "
                                + getText(co.tagtalk.winemate.R.string.authentication_activity_open_bottle_button_middle_right);

                        openBottle.setText(openBottleButtonText);
                        openBottle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("tagInfo", tagInfo);
                                intent.putExtra("wineId", wineId);
                                intent.putExtra("tagId", tagID);
                                intent.putExtra("userId", userId);
                                intent.putExtra("winePicURL", winePicURL);
                                intent.putExtra("rewardPoint", rewardPoint);
                                intent.setClassName("co.tagtalk.winemate",
                                        "co.tagtalk.winemate.QRScannerActivity");
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                } else {
                    winePic.setImageResource(R.drawable.wine_pic_open);
                    if (Locale.getDefault().getLanguage().equals("zh")) {
                        genuineSymbol.setImageResource(R.drawable.genuine_symbol_opened_zh);
                    } else {
                        genuineSymbol.setImageResource(R.drawable.genuine_symbol_opened_en);
                    }
                    String bottleOpenedReminder = getText(R.string.authentication_activity_result_opened_where)
                            + " " + wineInfo.openedCity + " " + wineInfo.openedCountry + " \n\n" +
                            getText(R.string.authentication_activity_result_opened_when)
                            + " " + wineInfo.openedTime + " \n\n" +
                            getText(R.string.authentication_activity_result_opened_caution);
                    result.setText(bottleOpenedReminder);
                    result.setGravity(Gravity.START);
                    contactUs.setVisibility(View.VISIBLE);
                    contactUs.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                    contactUs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClassName("co.tagtalk.winemate",
                                    "co.tagtalk.winemate.HelpDeskActivity");
                            startActivity(intent);
                        }
                    });
                }
            }
        }
    }

    public class GetTagPasswordTask extends AsyncTask<String, Void, String> {

        private Activity activity;

        public GetTagPasswordTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            String password = null;

            try {

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                password = client.getTagPassword(params[0]);
                badNetwork = false;

            } catch (TException x) {
                x.printStackTrace();
                AuthenticationActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        errorBadNetwork();
                    }
                });
            }
            transport.close();

            return password;
        }

        @Override
        protected void onPostExecute(String password) {

            if (password == null || password.isEmpty()) {
                if (!badNetwork) {
                    tagNoRecordProcessor();
                }
                return;
            }

            if (Configs.DEBUG_MODE) {
                Log.v("ZZZ Authentication", "password: " + password);
            }
            tagPassword = hexStringToByteArray(password);

            String authenticationCode = "";

            for (int i = 0; i < (AUTHENTICATION_CODE_LENGTH / PAGE_SIZE / 4); i++) {
                if (readTag(ultralight, AUTHENTICATION_CODE_PAGE_OFFSET + PAGE_SIZE * i, true) != null) {
                    authenticationCode += readTag(ultralight, AUTHENTICATION_CODE_PAGE_OFFSET + PAGE_SIZE * i, true);
                } else {
                    return;
                }
            }

            final TagInfo tagInfo = new TagInfo();
            tagInfo.tagID = tagID;
            tagInfo.secretNumber = authenticationCode;

            if (Locale.getDefault().getLanguage().equals("zh")) {
                tagInfo.countryId = CountryId.CHINESE;
            } else {
                tagInfo.countryId = CountryId.ENGLISH;
            }

            LocationService locationService = new LocationService(activity);
            LocationInfo locationInfo = locationService.getCurrentLocationInfo();
            String detailedLocation = locationInfo.getLatitude() + ", " + locationInfo.getLongitude();
            TimeStamp timeStamp = new TimeStamp();

            tagInfo.city = locationInfo.getCityName();
            tagInfo.date = timeStamp.getCurrentDate();
            tagInfo.time = timeStamp.getCurrentTime();
            tagInfo.detailedLocation = detailedLocation;

            if (Configs.DEBUG_MODE) {
                Log.v(LOG_TAG, "tagId = " + tagID);
                Log.v(LOG_TAG, "secretNumber = " + tagInfo.secretNumber);
            }

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AuthenticationActivity.this);

            if ((Configs.userId == 0) && (sharedPrefs.getInt(Configs.USER_ID, 0) == 0 || !sharedPrefs.getBoolean(Configs.LOGIN_STATUS, false))) {
                Toast.makeText(AuthenticationActivity.this, "Please log in or sign up first.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("tagInfo", tagInfo);

                intent.setClassName("co.tagtalk.winemate",
                        "co.tagtalk.winemate.LoginActivity");
                AuthenticationActivity.this.startActivity(intent);
                finish();
                return;
            } else if (sharedPrefs.getInt(Configs.USER_ID, 0) != 0) {
                userId = sharedPrefs.getInt(Configs.USER_ID, 0);
            } else {
                userId = Configs.userId;
            }

            final AuthenticationTask authenticationTask = new AuthenticationTask(activity);
            authenticationTask.execute(tagInfo, userId);
        }
    }

    private void tagNoRecordProcessor() {

        winePic = (ImageView) findViewById(R.id.authentication_wine_picture);
        genuineSymbol = (ImageView) findViewById(R.id.authentication_genuine_symbol);
        result = (TextView) findViewById(R.id.authentication_result_text);
        contactUs = (TextView) findViewById(R.id.authentication_contact_us);

        contactUsFake = (TextView) findViewById(R.id.authentication_contact_us_fake);
        wrongAuthCodeReminder = (TextView) findViewById(R.id.authentication_wrong_auth_code_reminder);

        authenticationRootLayout = (RelativeLayout) findViewById(R.id.authentication_content_root_relative_layout);
        progressBarLayout = (RelativeLayout) findViewById(R.id.authentication_progress_bar_relative_layout);

        progressBarLayout.setVisibility(GONE);
        authenticationRootLayout.setVisibility(View.VISIBLE);

        //Hide UI elements for genuine result
        if (openBottle != null) {
            openBottle.setVisibility(GONE);
        }
        if (wineName != null) {
            wineName.setVisibility(GONE);
        }
        if (wineYear != null) {
            wineYear.setVisibility(GONE);
        }
        if (wineRegion != null) {
            wineRegion.setVisibility(GONE);
        }
        if (viewMore != null) {
            viewMore.setVisibility(GONE);
        }
        if (wechatShare != null) {
            wechatShare.setVisibility(GONE);
        }
        if (wineRate != null) {
            wineRate.setVisibility(GONE);
        }

        winePic.setImageResource(R.drawable.wine_pic_warning);
        if (Locale.getDefault().getLanguage().equals("zh")) {
            genuineSymbol.setImageResource(R.drawable.genuine_symbol_warning_zh);
        } else {
            genuineSymbol.setImageResource(R.drawable.genuine_symbol_warning_en);
        }
        wrongAuthCodeReminder.setVisibility(View.VISIBLE);
        String resultString = getText(co.tagtalk.winemate.R.string.authentication_activity_result_fake) + "\n";
        result.setText(resultString);
        result.setTypeface(Typeface.DEFAULT_BOLD);
        contactUs.setVisibility(View.VISIBLE);
        contactUs.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        contactUsFake.setVisibility(View.VISIBLE);

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("co.tagtalk.winemate",
                        "co.tagtalk.winemate.HelpDeskActivity");
                startActivity(intent);
            }
        });
    }

    private void errorBadNetwork() {
        progressBarLayout.setVisibility(View.VISIBLE);
        authenticationRootLayout.setVisibility(View.INVISIBLE);
        badNetwork = true;
        if (warningText != null) {
            warningText.setVisibility(View.VISIBLE);
            warningText.setText(R.string.failed_to_do_operation);
        }
    }

    private void errorTagLost() {
        progressBarLayout.setVisibility(View.VISIBLE);
        authenticationRootLayout.setVisibility(View.INVISIBLE);
        if (warningText != null) {
            warningText.setVisibility(View.VISIBLE);
            warningText.setText(R.string.authentication_activity_hold_longer_time_warning);
        }
    }

    private String readTag(MifareUltralight ultralight, int offset, boolean isProtected) {
        try {
            ultralight.connect();

            if (isProtected) {
                authenticate(ultralight, tagPassword);
            }

            byte[] payloads = ultralight.readPages(offset);
            return byteArrayToHexString(payloads);

        } catch (TagLostException e) {
            Log.e("### error", "TagLostException while reading MifareUltralight message...", e);
            errorTagLost();
            return null;
        } catch (IOException e) {
            Log.e("### error", "IOException while reading MifareUltralight message...", e);
            errorTagLost();
            return null;
        } catch (NullPointerException e) {
            Log.e("### error", "NullPointerException while reading MifareUltralight message...", e);
            errorTagLost();
            return null;
        }
        finally {
            if (ultralight != null) {
                try {
                    ultralight.close();
                } catch (IOException e) {
                    Log.e("### error", "Error closing tag...", e);
                }
            }
        }
    }

    private void authenticate(MifareUltralight ultralight, byte[] password) {
        try {
            byte[] PWD_AUTH_CMD = new byte[password.length + 1];

            //PWD_AUTH code 1Bh [Ref.1 p46]
            PWD_AUTH_CMD[0] = 0x1B;

            int i = 1;
            for (byte b : password) {
                PWD_AUTH_CMD[i] = b;
                i++;
            }
            ultralight.transceive(PWD_AUTH_CMD);
        } catch (TagLostException e) {
            Log.e("### error", "TagLostException while authenticating message...", e);
            errorTagLost();
        } catch (IOException e) {
            Log.e("### error", "IOException while authenticating tag...", e);
        }
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

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}
