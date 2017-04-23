package co.tagtalk.winemate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.util.ArrayList;
import java.util.Locale;

import co.tagtalk.winemate.thriftfiles.AddToWishlistRequest;
import co.tagtalk.winemate.thriftfiles.FoodParingPics;
import co.tagtalk.winemate.thriftfiles.IsInWishlistResponse;
import co.tagtalk.winemate.thriftfiles.MyRateRecordRequest;
import co.tagtalk.winemate.thriftfiles.MyRateRecordResponse;
import co.tagtalk.winemate.thriftfiles.WineBasicInfoRequest;
import co.tagtalk.winemate.thriftfiles.WineBasicInfoResponse;
import co.tagtalk.winemate.thriftfiles.WineMateServices;
import co.tagtalk.winemate.thriftfiles.WineReviewAndRatingData;
import co.tagtalk.winemate.thriftfiles.WineReviewAndRatingReadRequest;
import co.tagtalk.winemate.thriftfiles.WineReviewAndRatingReadResponse;

public class WineInfoActivity extends AppCompatActivity {

    private Integer userId;
    private Integer wineId;
    private String winePicURL;
    private String  tagId;
    private boolean isInWishlist;
    ScrollView wineInfoScrollView;
    ProgressBar wineInfoProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wine_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wineInfoScrollView = (ScrollView)findViewById(R.id.wine_info_scroll_view);
        wineInfoProgressBar = (ProgressBar)findViewById(R.id.wine_info_progress);

        Button myBottlesButton = (Button) findViewById(R.id.wine_info_bottle);
        Button homeButton = (Button) findViewById(R.id.wine_info_home);
        Button myFriendListButton = (Button) findViewById(R.id.wine_info_group);
        Button addToMyBottlesButton = (Button) findViewById(R.id.wine_info_add_to_my_bottles_button);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WineInfoActivity.this);

        if (sharedPrefs.getInt(Configs.USER_ID, 0) != 0 && sharedPrefs.getBoolean(Configs.LOGIN_STATUS, false)) {
            userId = sharedPrefs.getInt(Configs.USER_ID, 0);
        } else {
            userId = Configs.userId;
        }

        Intent intent= getIntent();
        wineId = intent.getIntExtra("wineId", 0);
        winePicURL = intent.getStringExtra("winePicURL");
        tagId      = intent.getStringExtra("tagId");

        boolean isSealed = intent.getBooleanExtra("isSealed", false);

        if (Locale.getDefault().getLanguage().equals("zh")) {
            Configs.COUNTRY_ID = Constants.COUNTRY_ID_CHINESE;
        } else {
            Configs.COUNTRY_ID = Constants.COUNTRY_ID_ENGLISH;
        }

        ImageView winePic;
        winePic = (ImageView)this.findViewById(R.id.wine_info_wine_picture);
        Picasso.with(this).load(winePicURL)
                .error(R.drawable.placeholder)
                .into(winePic);

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

        if (addToMyBottlesButton != null) {

            if (isSealed) {
                addToMyBottlesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("wineId", wineId);
                        intent.putExtra("tagId", tagId);
                        intent.putExtra("userId", userId);
                        intent.putExtra("winePicURL", winePicURL);
                        intent.setClassName("co.tagtalk.winemate",
                                "co.tagtalk.winemate.QRScannerActivity");
                        startActivity(intent);
                    }
                });
            } else {
                addToMyBottlesButton.setVisibility(View.INVISIBLE);
            }
        }

        WineBasicInfoRequest wineBasicInfoRequest = new WineBasicInfoRequest(wineId, Configs.COUNTRY_ID);
        final GetBasicInfoTask getBasicInfoTask = new GetBasicInfoTask(WineInfoActivity.this);
        getBasicInfoTask.execute(wineBasicInfoRequest);

        final CheckIsInWishlistTask checkIsInWishlistTask = new CheckIsInWishlistTask(WineInfoActivity.this);
        checkIsInWishlistTask.execute(userId, wineId);

        WineReviewAndRatingReadRequest wineReviewAndRatingReadRequest = new WineReviewAndRatingReadRequest(wineId, userId);
        final GetWineReviewAndRatingTask getWineReviewAndRatingTask = new GetWineReviewAndRatingTask(WineInfoActivity.this);
        getWineReviewAndRatingTask.execute(wineReviewAndRatingReadRequest);

        MyRateRecordRequest myRateRecordRequest = new MyRateRecordRequest(userId, wineId);
        final GetMyRateRecordTask getMyRateRecordTask = new GetMyRateRecordTask(WineInfoActivity.this);
        getMyRateRecordTask.execute(myRateRecordRequest);
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
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WineInfoActivity.this);
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

    public class GetBasicInfoTask extends AsyncTask<WineBasicInfoRequest, Void, WineBasicInfoResponse> {

        private Activity activity;
        private ImageView nationalFlag;
        private ImageView wineryLogo;
        private TextView wineryName;
        private TextView wineName;
        private TextView wineLocation;
        private TextView wineInfo;
        private TextView foodPairingInfo;
        private TextView cellaringInfo;
        private TextView regionName;
        private TextView regionInfo;
        private TextView wineryInfo;
        private TextView grapeVarieties;
        // Disable Average Price for now.
        // private TextView averagePrice;
        private ImageButton shoppingCart;
        private ImageButton wechatShare;
        private RecyclerView foodPairingPicsRecyclerView;
        private WineInfoFoodPairingPicsRecyclerViewAdapter wineInfoFoodPairingPicsRecyclerViewAdapter;
        private boolean gotException;

        public GetBasicInfoTask(Activity activity) {
            this.activity = activity;

            nationalFlag = (ImageView)this.activity.findViewById(R.id.wine_info_national_flag);
            wineryLogo = (ImageView)this.activity.findViewById(R.id.wine_info_winery_logo);
            wineryName = (TextView)this.activity.findViewById(R.id.wine_info_winery_name);
            wineName = (TextView)this.activity.findViewById(R.id.wine_info_wine_name);
            wineLocation = (TextView)this.activity.findViewById(R.id.wine_info_wine_location);
            wineInfo = (TextView)this.activity.findViewById(R.id.wine_info_the_wine_text);
            foodPairingInfo = (TextView)this.activity.findViewById(R.id.wine_info_food_pairing_text);
            cellaringInfo = (TextView)this.activity.findViewById(R.id.wine_info_cellaring_text);
            regionName = (TextView)this.activity.findViewById(R.id.wine_info_region_name);
            regionInfo = (TextView)this.activity.findViewById(R.id.wine_info_region_info);
            wineryInfo = (TextView)this.activity.findViewById(R.id.wine_info_winery_info);
            grapeVarieties = (TextView)this.activity.findViewById(R.id.wine_info_grape_varieties);
            foodPairingPicsRecyclerView = (RecyclerView) findViewById(R.id.wine_info_food_pairing_pictures_recycler_view);
            // Disable Average Price for now.
            // averagePrice = (TextView)this.activity.findViewById(R.id.wine_info_average_price_number);
            shoppingCart = (ImageButton) this.activity.findViewById(R.id.wine_info_shopping_cart);
            wechatShare = (ImageButton) this.activity.findViewById(R.id.wine_info_share_content);
        }

        @Override
        protected WineBasicInfoResponse doInBackground(WineBasicInfoRequest... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            WineBasicInfoResponse wineBasicInfoResponse = new WineBasicInfoResponse();

            try{

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                wineBasicInfoResponse = client.getBasicInfo(params[0]);


            } catch (TException x) {
                x.printStackTrace();
                gotException = true;
            }
            transport.close();

            return wineBasicInfoResponse;
        }

        @Override
        protected void onPostExecute(final WineBasicInfoResponse wineBasicInfoResponse) {
            if (gotException) {
                wineInfoProgressBar.setVisibility(View.GONE);
                wineInfoScrollView.setVisibility(View.VISIBLE);
                Toast.makeText(this.activity, R.string.failed_to_get_remote_data, Toast.LENGTH_LONG).show();
                return;
            }

            if (Configs.DEBUG_MODE) {
                Log.v("ZZZ WineInfo", "WineName: " + wineBasicInfoResponse.wineName);
                Log.v("ZZZ WineInfo", "wineryName: " + wineBasicInfoResponse.wineryName);
                Log.v("ZZZ WineInfo", "location: " + wineBasicInfoResponse.location);
                Log.v("ZZZ WineInfo", "nationalFlagUrl: " + wineBasicInfoResponse.nationalFlagUrl);
                Log.v("ZZZ WineInfo", "theWineInfo: " + wineBasicInfoResponse.theWineInfo);
                Log.v("ZZZ WineInfo", "foodPairingInfo: " + wineBasicInfoResponse.foodPairingInfo);
                Log.v("ZZZ WineInfo", "foodParingPics: " + wineBasicInfoResponse.foodParingPics);
                Log.v("ZZZ WineInfo", "regionInfo: " + wineBasicInfoResponse.regionInfo);
                Log.v("ZZZ WineInfo", "wineryWebsiteUrl: " + wineBasicInfoResponse.wineryWebsiteUrl);
                Log.v("ZZZ WineInfo", "wineryLogoPicUrl: " + wineBasicInfoResponse.wineryLogoPicUrl);
                Log.v("ZZZ WineInfo", "grapeInfo: " + wineBasicInfoResponse.grapeInfo);
                // Disable Average Price for now.
                // Log.v("ZZZ WineInfo", "averagePrice: " + wineBasicInfoResponse.averagePrice);
            }

            if (wineBasicInfoResponse.nationalFlagUrl != null && !wineBasicInfoResponse.nationalFlagUrl.isEmpty()) {
                Picasso.with(this.activity).load(wineBasicInfoResponse.nationalFlagUrl)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .error(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .into(nationalFlag);
            }


            if (wineBasicInfoResponse.wineryLogoPicUrl != null && !wineBasicInfoResponse.wineryLogoPicUrl.isEmpty()) {
                Picasso.with(this.activity).load(wineBasicInfoResponse.wineryLogoPicUrl)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .error(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .into(wineryLogo);
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.activity, LinearLayoutManager.HORIZONTAL, false);
            foodPairingPicsRecyclerView.setLayoutManager(linearLayoutManager);
            wineInfoFoodPairingPicsRecyclerViewAdapter = new WineInfoFoodPairingPicsRecyclerViewAdapter(this.activity, new ArrayList<FoodParingPics>());
            foodPairingPicsRecyclerView.setAdapter(wineInfoFoodPairingPicsRecyclerViewAdapter);

            if (wineBasicInfoResponse.foodParingPics != null) {
                wineInfoFoodPairingPicsRecyclerViewAdapter.loadPicture(wineBasicInfoResponse.foodParingPics);
            }

            wineryName.setText(wineBasicInfoResponse.wineryName);
            wineName.setText(wineBasicInfoResponse.wineName + " " +wineBasicInfoResponse.year);
            wineLocation.setText(wineBasicInfoResponse.location);

            // Disable Average Price for now.
            // averagePrice.setText(wineBasicInfoResponse.averagePrice);

            final String wineryName = wineBasicInfoResponse.wineryName;

            wineryLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("wineryName", wineryName);
                    intent.setClassName("co.tagtalk.winemate",
                            "co.tagtalk.winemate.WineryInfoActivity");
                    startActivity(intent);
                }
            });
            String wineInfoTxt = "<b>" + getString(R.string.wine_info_activity_the_wine) + "</b>" + wineBasicInfoResponse.theWineInfo;
            wineInfo.setText(Html.fromHtml(wineInfoTxt));

            String foodPairingInfoTxt = "<b>" + getString(R.string.wine_info_activity_food_pairing) + "</b>" + wineBasicInfoResponse.foodPairingInfo;
            foodPairingInfo.setText(Html.fromHtml(foodPairingInfoTxt));

            String cellaringInfoTxt = "<b>" + getString(R.string.wine_info_activity_cellaring) + "</b>" + wineBasicInfoResponse.cellaringInfo;
            cellaringInfo.setText(Html.fromHtml(cellaringInfoTxt));

            String regionNameTxt = "<b>" + getString(R.string.wine_info_activity_region) + "</b>" + wineBasicInfoResponse.regionName;
            regionName.setText(Html.fromHtml(regionNameTxt));

            regionInfo.setText(wineBasicInfoResponse.regionInfo);

            String wineryInfoTxt = "<b>" + getString(R.string.wine_info_activity_winery) + "</b>" + wineBasicInfoResponse.location;
            wineryInfo.setText(Html.fromHtml(wineryInfoTxt));

            String grapeVarietiesTxt = "<b>" + getString(R.string.wine_info_activity_grape_varieties) + "</b>" + wineBasicInfoResponse.grapeInfo;
            grapeVarieties.setText(Html.fromHtml(grapeVarietiesTxt));

            // Show the scroll bar content right after basic contents are filled. Others can wait.
            wineInfoProgressBar.setVisibility(View.GONE);
            wineInfoScrollView.setVisibility(View.VISIBLE);

            shoppingCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(WineInfoActivity.this, "TBD: Adding online shopping option", Toast.LENGTH_LONG).show();
                }
            });

            wechatShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("wineId", wineId);
                    intent.putExtra("url",wineBasicInfoResponse.wechatShareUrl);
                    intent.putExtra("getRewards", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setClassName("co.tagtalk.winemate",
                            "co.tagtalk.winemate.ShareDialogActivity");
                    startActivity(intent);
                }
            });
        }
    }

    public class CheckIsInWishlistTask extends AsyncTask<Integer, Void, IsInWishlistResponse> {

        private Activity activity;
        private ImageButton addToWishlist;

        public CheckIsInWishlistTask(Activity activity) {
            this.activity = activity;
            this.addToWishlist = (ImageButton) this.activity.findViewById(R.id.wine_info_add_to_wishlist);
        }

        @Override
        protected IsInWishlistResponse doInBackground(Integer... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            IsInWishlistResponse response = new IsInWishlistResponse();

            try{

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                response = client.isInWishlist(params[0], params[1]);

            } catch (TException x) {
                x.printStackTrace();
            }
            transport.close();

            return response;
        }

        @Override
        protected void onPostExecute(IsInWishlistResponse response) {
            if (!response.success) {
                Log.v("isInWishlist: ", "Service call failed with error.");
            }
            isInWishlist = response.isInList;
            if (isInWishlist) {
                addToWishlist.setImageResource(R.drawable.ic_wishlist_add_blue_24dp);
            }
            addToWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddToWishlistRequest addToWishlistRequest = new AddToWishlistRequest(userId, wineId, !isInWishlist);
                    final AddToWishlistTask addToWishlistTask = new AddToWishlistTask(activity);
                    addToWishlistTask.execute(addToWishlistRequest);
                    // Switch the color first. Will switch back if operation failed.
                    addToWishlist.setImageResource(isInWishlist ? R.drawable.ic_wishlist_add_black_24dp : R.drawable.ic_wishlist_add_blue_24dp);
                    // Disable On Click Listener. Will enable after add/remove operation is done.
                    addToWishlist.setOnClickListener(null);
                }
            });
        }
    }

    public class AddToWishlistTask extends AsyncTask<AddToWishlistRequest, Void, Boolean> {

        private Activity activity;
        private ImageButton addToWishlist;

        public AddToWishlistTask(Activity activity) {
            this.activity = activity;
            this.addToWishlist = (ImageButton) this.activity.findViewById(R.id.wine_info_add_to_wishlist);
        }

        @Override
        protected Boolean doInBackground(AddToWishlistRequest... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            Boolean response = false;

            try{

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                response = client.addToWishlist(params[0]);

            } catch (TException x) {
                x.printStackTrace();
            }
            transport.close();

            return response;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!isInWishlist) {
                if (success) {
                    Toast.makeText(WineInfoActivity.this, getString(R.string.added_to_wishlist_text), Toast.LENGTH_SHORT).show();
                    isInWishlist = true;
                } else {
                    // Switch color back if failed.
                    addToWishlist.setImageResource(R.drawable.ic_wishlist_add_black_24dp);
                    Toast.makeText(WineInfoActivity.this, getString(R.string.failed_to_add_to_wishlist_text), Toast.LENGTH_SHORT).show();
                }
            } else {
                if (success) {
                    Toast.makeText(WineInfoActivity.this, getString(R.string.removed_from_wishlist_text), Toast.LENGTH_SHORT).show();
                    isInWishlist = false;
                } else {
                    // Switch color back if failed.
                    addToWishlist.setImageResource(R.drawable.ic_wishlist_add_blue_24dp);
                    Toast.makeText(WineInfoActivity.this, getString(R.string.failed_to_remove_from_wishlist_text), Toast.LENGTH_SHORT).show();
                }
            }
            addToWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddToWishlistRequest addToWishlistRequest = new AddToWishlistRequest(userId, wineId, !isInWishlist);
                    final AddToWishlistTask addToWishlistTask = new AddToWishlistTask(activity);
                    addToWishlistTask.execute(addToWishlistRequest);
                    // Switch the color first. Will switch back if operation failed.
                    addToWishlist.setImageResource(isInWishlist ? R.drawable.ic_wishlist_add_black_24dp : R.drawable.ic_wishlist_add_blue_24dp);
                    // Disable On Click Listener. Will enable after add/remove operation is done.
                    addToWishlist.setOnClickListener(null);
                }
            });
        }
    }

    public class GetWineReviewAndRatingTask extends AsyncTask<WineReviewAndRatingReadRequest, Void, WineReviewAndRatingReadResponse> {

        private Activity activity;
        private TextView averageRatingNumber;
        private TextView totalRatingNumber;
        private RatingBar averageRatingBar;
        private RecyclerView wineInfoReviewsRecyclerView;
        private WineInfoReviewsRecyclerViewAdapter wineInfoReviewsRecyclerViewAdapter;

        public GetWineReviewAndRatingTask(Activity activity) {
            this.activity = activity;
            averageRatingNumber = (TextView) this.activity.findViewById(R.id.wine_info_average_rating_number);
            totalRatingNumber = (TextView) this.activity.findViewById(R.id.wine_info_total_rating_number);
            averageRatingBar = (RatingBar)this.activity.findViewById(R.id.wine_info_average_rating_bar_indicator);
            wineInfoReviewsRecyclerView = (RecyclerView) findViewById(R.id.wine_info_reviews_recycler_view);
        }

        @Override
        protected WineReviewAndRatingReadResponse doInBackground(WineReviewAndRatingReadRequest... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            WineReviewAndRatingReadResponse wineReviewAndRatingReadResponse = new WineReviewAndRatingReadResponse();

            try{

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                wineReviewAndRatingReadResponse = client.getWineReviewAndRating(params[0]);


            } catch (TException x) {
                x.printStackTrace();
            }
            transport.close();

            return wineReviewAndRatingReadResponse;
        }

        @Override
        protected void onPostExecute(WineReviewAndRatingReadResponse wineReviewAndRatingReadResponse) {
            super.onPostExecute(wineReviewAndRatingReadResponse);

            if (Configs.DEBUG_MODE) {
                Log.v("ZZZ WineInfo R N R: ", "list of data: " + wineReviewAndRatingReadResponse.data);
                Log.v("ZZZ WineInfo R N R: ", "numOfRating: " + wineReviewAndRatingReadResponse.numOfRating);
                Log.v("ZZZ WineInfo R N R: ", "numOfReview: " + wineReviewAndRatingReadResponse.numOfReview);
                Log.v("ZZZ WineInfo R N R: ", "averageRate: " + wineReviewAndRatingReadResponse.averageRate);

            }

            averageRatingNumber.setText(String.valueOf(wineReviewAndRatingReadResponse.averageRate));

            if (wineReviewAndRatingReadResponse.numOfRating <= 1) {
                String totalRatings = String.valueOf(wineReviewAndRatingReadResponse.numOfRating) + " Rating";
                totalRatingNumber.setText(totalRatings);
            } else {
                String totalRatings = String.valueOf(wineReviewAndRatingReadResponse.numOfRating) + " Ratings";
                totalRatingNumber.setText(totalRatings);
            }

            averageRatingBar.setRating((float) wineReviewAndRatingReadResponse.averageRate);


            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false);
            wineInfoReviewsRecyclerView.setLayoutManager(linearLayoutManager);
            wineInfoReviewsRecyclerViewAdapter = new WineInfoReviewsRecyclerViewAdapter(this.activity, new ArrayList<WineReviewAndRatingData>());
            wineInfoReviewsRecyclerView.setAdapter(wineInfoReviewsRecyclerViewAdapter);
            wineInfoReviewsRecyclerView.setNestedScrollingEnabled(false);
            wineInfoReviewsRecyclerViewAdapter.loadData(wineReviewAndRatingReadResponse.data);

            wineInfoReviewsRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this.activity, wineInfoReviewsRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent();
                    intent.putExtra("requestedId", wineInfoReviewsRecyclerViewAdapter.getUserId(position));
                    intent.setClassName("co.tagtalk.winemate",
                            "co.tagtalk.winemate.UserProfileActivity");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }));

        }
    }


    public class GetMyRateRecordTask extends AsyncTask<MyRateRecordRequest, Void, MyRateRecordResponse> {

        private Activity activity;
        private RatingBar wineInfoRatingBar;
        private TextView  wineInfoRatingBarText;
        private TextView  wineInfoReRate;
        private Button    addReviews;

        public GetMyRateRecordTask(Activity activity) {
            this.activity = activity;
            wineInfoRatingBar = (RatingBar)findViewById(R.id.wine_info_rating_bar);
            wineInfoRatingBarText = (TextView)findViewById(R.id.wine_info_rating_bar_text);
            wineInfoReRate = (TextView)findViewById(R.id.wine_info_re_rate);
            addReviews = (Button)findViewById(R.id.wine_info_add_review_button);
        }

        @Override
        protected MyRateRecordResponse doInBackground(MyRateRecordRequest... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            MyRateRecordResponse myRateRecordResponse = new MyRateRecordResponse();

            try{

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                myRateRecordResponse = client.getMyRateRecord(params[0]);


            } catch (TException x) {
                x.printStackTrace();
            }
            transport.close();

            return myRateRecordResponse;
        }

        @Override
        protected void onPostExecute(MyRateRecordResponse myRateRecordResponse) {
            super.onPostExecute(myRateRecordResponse);

            if (wineInfoRatingBar != null) {
                if (myRateRecordResponse.alreadyRated) {
                    final float rating = (float)myRateRecordResponse.myRate;
                    wineInfoRatingBar.setIsIndicator(true);
                    wineInfoRatingBar.setRating((float)myRateRecordResponse.myRate);
                    wineInfoRatingBarText.setText(R.string.wine_info_activity_rating_bar_already_rated_text);
                    wineInfoReRate.setVisibility(View.VISIBLE);
                    wineInfoReRate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra("rating", rating);
                            intent.putExtra("userId", userId);
                            intent.putExtra("wineId", wineId);
                            intent.putExtra("winePicURL", winePicURL);
                            intent.setClassName("co.tagtalk.winemate",
                                    "co.tagtalk.winemate.WriteRatingAndReviewActivity");
                            startActivity(intent);
                        }
                    });

                } else {
                    wineInfoRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                            wineInfoReRate.setVisibility(View.GONE);
                            Intent intent = new Intent();
                            intent.putExtra("rating", rating);
                            intent.putExtra("userId", userId);
                            intent.putExtra("wineId", wineId);
                            intent.putExtra("winePicURL", winePicURL);
                            intent.setClassName("co.tagtalk.winemate",
                                    "co.tagtalk.winemate.WriteRatingAndReviewActivity");
                            startActivity(intent);
                        }
                    });
                }
            }

            if (addReviews != null) {
                final float rating;
                if (myRateRecordResponse.alreadyRated) {
                    rating = (float)myRateRecordResponse.myRate;
                } else {
                    rating = 0;
                }
                addReviews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putExtra("rating", rating);
                        intent.putExtra("userId", userId);
                        intent.putExtra("wineId", wineId);
                        intent.putExtra("winePicURL", winePicURL);
                        intent.setClassName("co.tagtalk.winemate",
                                "co.tagtalk.winemate.WriteRatingAndReviewActivity");
                        startActivity(intent);
                    }
                });
            }

            if (Configs.DEBUG_MODE) {
                Log.v("ZZZ WineInfo myreview: ", "list of data: " + myRateRecordResponse.alreadyRated);
                Log.v("ZZZ WineInfo myreview: ", "list of data: " + myRateRecordResponse.myRate);
            }
        }
    }
}
