package co.tagtalk.winemate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.util.ArrayList;

import co.tagtalk.winemate.thriftfiles.BottleInfo;
import co.tagtalk.winemate.thriftfiles.FriendInfo;
import co.tagtalk.winemate.thriftfiles.FriendListRequest;
import co.tagtalk.winemate.thriftfiles.MyBottlesRequest;
import co.tagtalk.winemate.thriftfiles.MyFollowersListResponse;
import co.tagtalk.winemate.thriftfiles.MyFollowingListResponse;
import co.tagtalk.winemate.thriftfiles.MyProfile;
import co.tagtalk.winemate.thriftfiles.MyWishListResponse;
import co.tagtalk.winemate.thriftfiles.RatedBottlesResponse;
import co.tagtalk.winemate.thriftfiles.ThirdParty;
import co.tagtalk.winemate.thriftfiles.User;
import co.tagtalk.winemate.thriftfiles.WineMateServices;

import static android.view.View.GONE;


public class UserProfileActivity extends AppCompatActivity {

    private Integer userId;
    private Integer requestedId;
    private TextView followersNumber;
    private TextView followingNumber;
    private TextView ratingNumber;
    private RelativeLayout ratingView;
    private TextView ratingText;
    private TextView followersText;
    private TextView followingText;
    private TextView wishlistText;
    private Button followButton;
    private RecyclerView myWishlistRecyclerView;
    private MyBottlesRecyclerViewAdapter myWishlistRecyclerViewAdapter;
    private Button uploadPhotoButton;
    private boolean refreshUserIcon;
    RelativeLayout userProfileRelativeLayout;
    ProgressBar userProfileProgressBar;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh wish list if selected. This is for the case that the user removes wine from wish
        // list from wine info page, and then go back to user profile page with Wish List tab selected.
        if (wishlistText.getTextColors() == ContextCompat.getColorStateList(this, R.color.text_black)) {
            final RelativeLayout wishlistView = (RelativeLayout)findViewById(R.id.user_profile_wishlist_view);
            wishlistView.callOnClick();
        }

        if (sharedPrefs.getInt(Configs.USER_ID, 0) == 83) {
            Toast.makeText(this, "UserProfileActivity onResume(), requestedId = " + requestedId.toString(), Toast.LENGTH_LONG).show();
        }

        final GetMyProfileTask getMyProfileTask = new GetMyProfileTask(UserProfileActivity.this);
        if (requestedId == 0) {
            getMyProfileTask.execute(userId, userId);
        } else {
            getMyProfileTask.execute(userId, requestedId);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userProfileRelativeLayout = (RelativeLayout)findViewById(R.id.content_user_profile);
        userProfileProgressBar = (ProgressBar)findViewById(R.id.user_profile_progress);

        Intent intent= getIntent();
        refreshUserIcon = intent.getBooleanExtra("refreshUserIcon", false);

        requestedId = intent.getIntExtra("requestedId", 0);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this);

        if (sharedPrefs.getInt(Configs.USER_ID, 0) != 0 && sharedPrefs.getBoolean(Configs.LOGIN_STATUS, false)) {
            userId = sharedPrefs.getInt(Configs.USER_ID, 0);
        } else {
            userId = Configs.userId;
        }

        final RelativeLayout summaryView = (RelativeLayout)findViewById(R.id.user_profile_summary_view);
        final RelativeLayout wishlistView = (RelativeLayout)findViewById(R.id.user_profile_wishlist_view);
        final RelativeLayout followersView = (RelativeLayout)findViewById(R.id.user_profile_followers_view);
        final RelativeLayout followingView = (RelativeLayout)findViewById(R.id.user_profile_following_view);
        final LinearLayout userProfileButtonsLayout2 = (LinearLayout)findViewById(R.id.user_profile_buttons_layout_2);
        final RecyclerView userProfileRecyclerViewFollowerList = (RecyclerView) findViewById(R.id.user_profile_recycler_view_follower_list);
        final RecyclerView userProfileRecyclerViewFollowingList = (RecyclerView) findViewById(R.id.user_profile_recycler_view_following_list);
        final RecyclerView userProfileRecyclerViewRatingList = (RecyclerView) findViewById(R.id.user_profile_recycler_view_rating_list);

        final ImageView summaryIcon = (ImageView)findViewById(R.id.user_profile_summary_icon);
        final TextView summaryText = (TextView)findViewById(R.id.user_profile_summary_text);
        final ImageView wishlistIcon = (ImageView)findViewById(R.id.user_profile_wishlist_icon);

        wishlistText = (TextView)findViewById(R.id.user_profile_wishlist_text);
        followersNumber = (TextView)findViewById(R.id.user_profile_followers_number);
        followingNumber = (TextView)findViewById(R.id.user_profile_following_number);
        ratingNumber = (TextView)findViewById(R.id.user_profile_rating_number);
        ratingView = (RelativeLayout)findViewById(R.id.user_profile_rating_view);
        ratingText = (TextView)findViewById(R.id.user_profile_rating_text);
        followersText = (TextView)findViewById(R.id.user_profile_followers_text);
        followingText = (TextView)findViewById(R.id.user_profile_following_text);
        uploadPhotoButton = (Button)findViewById(R.id.user_profile_upload_photo);

        if (sharedPrefs.getInt(Configs.USER_ID, 0) == 83) {
            Toast.makeText(this, "UserProfileActivity onCreate(), requestedId = " + requestedId.toString(), Toast.LENGTH_LONG).show();
        }

        final GetMyProfileTask getMyProfileTask = new GetMyProfileTask(UserProfileActivity.this);
        if (requestedId == 0) {
            getMyProfileTask.execute(userId, userId);
        } else {
            getMyProfileTask.execute(userId, requestedId);
        }

        // Show wish list item delete button only if this the the current user's own profile page.
        final boolean showDeleteButton = (requestedId == 0 || requestedId.equals(userId));
        final boolean showUploadPhotoButton = (requestedId == 0 || requestedId.equals(userId));

        if (Configs.DEBUG_MODE) {
            Log.v("Arthur Debug ", "requested id: " + requestedId + " userId: " + userId + "\n");
        }
        myWishlistRecyclerView = (RecyclerView) findViewById(R.id.user_profile_recycler_view_wishlist);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myWishlistRecyclerView.setLayoutManager(linearLayoutManager);
        myWishlistRecyclerViewAdapter = new MyBottlesRecyclerViewAdapter(UserProfileActivity.this, new ArrayList<BottleInfo>(), showDeleteButton);
        myWishlistRecyclerView.setNestedScrollingEnabled(false);
        myWishlistRecyclerView.setAdapter(myWishlistRecyclerViewAdapter);

        summaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryIcon.setImageResource(R.drawable.summary_108x108_black);
                summaryText.setTextColor(getResources().getColor(R.color.text_black));
                wishlistIcon.setImageResource(R.drawable.wishlist_108x108_grey);
                wishlistText.setTextColor(getResources().getColor(R.color.text_gray));

                userProfileButtonsLayout2.setVisibility(View.VISIBLE);
                myWishlistRecyclerView.setVisibility(GONE);
            }
        });


        wishlistView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryIcon.setImageResource(R.drawable.summary_108x108_grey);
                summaryText.setTextColor(getResources().getColor(R.color.text_gray));
                wishlistIcon.setImageResource(R.drawable.wishlist_108x108_black);
                wishlistText.setTextColor(getResources().getColor(R.color.text_black));

                myWishlistRecyclerView.setVisibility(View.VISIBLE);
                userProfileButtonsLayout2.setVisibility(GONE);
                userProfileRecyclerViewFollowerList.setVisibility(GONE);
                userProfileRecyclerViewFollowingList.setVisibility(GONE);
                userProfileRecyclerViewRatingList.setVisibility(GONE);

                final MyBottlesRequest myBottlesRequest = new MyBottlesRequest(requestedId != 0 ? requestedId : userId, Configs.COUNTRY_ID);
                final GetMyWishlistTask getMyWishlistTask = new GetMyWishlistTask(UserProfileActivity.this);
                getMyWishlistTask.execute(myBottlesRequest);
            }
        });

        final FriendListRequest friendListRequest;
        if (requestedId == 0) {
            friendListRequest = new FriendListRequest(userId);
        } else {
            friendListRequest = new FriendListRequest(requestedId);
        }

        followersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followersNumber.setTextColor(getResources().getColor(R.color.text_black));
                followersText.setTextColor(getResources().getColor(R.color.text_black));
                followingNumber.setTextColor(getResources().getColor(R.color.text_gray));
                followingText.setTextColor(getResources().getColor(R.color.text_gray));
                ratingNumber.setTextColor(getResources().getColor(R.color.text_gray));
                ratingText.setTextColor(getResources().getColor(R.color.text_gray));

                final GetMyFollowersListTask getMyFollowersListTask = new  GetMyFollowersListTask(UserProfileActivity.this);
                getMyFollowersListTask.execute(friendListRequest);
            }
        });

        followingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followersNumber.setTextColor(getResources().getColor(R.color.text_gray));
                followersText.setTextColor(getResources().getColor(R.color.text_gray));
                followingNumber.setTextColor(getResources().getColor(R.color.text_black));
                followingText.setTextColor(getResources().getColor(R.color.text_black));
                ratingNumber.setTextColor(getResources().getColor(R.color.text_gray));
                ratingText.setTextColor(getResources().getColor(R.color.text_gray));

                final GetMyFollowingListTask getMyFollowingListTask = new  GetMyFollowingListTask(UserProfileActivity.this);
                getMyFollowingListTask.execute(friendListRequest);
            }
        });

        Button myBottlesButton = (Button) findViewById(R.id.user_profile_bottle);
        Button homeButton = (Button) findViewById(R.id.user_profile_home);
        Button myFriendListButton = (Button) findViewById(R.id.user_profile_group) ;

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

        if (showUploadPhotoButton) {
            if (uploadPhotoButton != null) {
                uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClassName("co.tagtalk.winemate",
                                "co.tagtalk.winemate.UploadPhotoActivity");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        } else {
            uploadPhotoButton.setVisibility(GONE);
        }

        summaryView.callOnClick();
        followersView.callOnClick();
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
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this);
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


    public class GetMyProfileTask extends AsyncTask<Integer, Void, MyProfile> {

        private Activity activity;

        private RelativeLayout privateContents;
        private TextView name;
        private TextView userName;
        private TextView email;
        private TextView gender;
        private TextView dob;
        private TextView points;
        private ImageView userIcon;
        private boolean gotException;

        public GetMyProfileTask(Activity activity) {
            this.activity = activity;

            privateContents = (RelativeLayout)this.activity.findViewById(R.id.user_profile_private_contents);
            name = (TextView)this.activity.findViewById(R.id.user_profile_name);
            userName = (TextView)this.activity.findViewById(R.id.user_profile_user_name);
            email = (TextView)this.activity.findViewById(R.id.user_profile_user_email);
            gender = (TextView)this.activity.findViewById(R.id.user_profile_user_gender);
            dob = (TextView)this.activity.findViewById(R.id.user_profile_user_dob);
            points = (TextView)this.activity.findViewById(R.id.user_profile_reward_points);
            userIcon = (ImageView)this.activity.findViewById(R.id.user_profile_user_icon);
            followButton = (Button)findViewById(R.id.user_profile_follow);
        }

        @Override
        protected MyProfile doInBackground(Integer... params) {
            if (sharedPrefs.getInt(Configs.USER_ID, 0) == 83) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(UserProfileActivity.this, "GetMyProfileTask doInBackground()", Toast.LENGTH_LONG).show();
                    }
                });
            }

            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            MyProfile myProfile = new MyProfile();

            if (sharedPrefs.getInt(Configs.USER_ID, 0) == 83) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(UserProfileActivity.this, "TSocket created", Toast.LENGTH_LONG).show();
                    }
                });
            }

            try{
                if (Configs.DEBUG_MODE) {
                    Log.v("ZZZ UserProfile: ", "getMyProfileRequest ");
                }
                transport.open();
                if (sharedPrefs.getInt(Configs.USER_ID, 0) == 83) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(UserProfileActivity.this, "transport.opened()", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                myProfile = client.getMyProfile(params[0], params[1]);

                if (sharedPrefs.getInt(Configs.USER_ID, 0) == 83) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (sharedPrefs.getInt(Configs.USER_ID, 0) == 83) {
                                Toast.makeText(UserProfileActivity.this, "client.getMyProfile is called", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            } catch (TException x) {
                if (sharedPrefs.getInt(Configs.USER_ID, 0) == 83) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (sharedPrefs.getInt(Configs.USER_ID, 0) == 83) {
                                Toast.makeText(UserProfileActivity.this, "Got TException", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                x.printStackTrace();
                gotException = true;
            }
            transport.close();

            return myProfile;
        }

        @Override
        protected void onPostExecute(final MyProfile myProfile) {
            super.onPostExecute(myProfile);

            if (gotException) {
                userProfileProgressBar.setVisibility(View.GONE);
                userProfileRelativeLayout.setVisibility(View.VISIBLE);
                Toast.makeText(this.activity, R.string.failed_to_get_remote_data, Toast.LENGTH_LONG).show();
                return;
            }

            if (Configs.DEBUG_MODE) {
                Log.v("ZZZ UserProfile", "USER: " + myProfile);
            }

            // If the use is a wechat user, show nickname here.
            String displayNameString = myProfile.getUser().getThirdParty() == ThirdParty.WECHAT ?
                    myProfile.getUser().getFirstName() : myProfile.getUser().getUserName();
            userName.setText(displayNameString);

            String nameString = getString(R.string.user_profile_name) + " " + myProfile.getUser().getFirstName() + " " + myProfile.getUser().getLastName();
            name.setText(nameString);

            if (sharedPrefs.getInt(Configs.USER_ID, 0) == 83) {
                Toast.makeText(UserProfileActivity.this, "Set displayNameString to -> " + displayNameString, Toast.LENGTH_LONG).show();
            }

            String emailString = getString(R.string.user_profile_user_email) + " " + myProfile.getUser().getEmail();
            email.setText(emailString);

            String genderString = getString(R.string.user_profile_user_gender) + " " + myProfile.getUser().getSex();
            gender.setText(genderString);

            String dobString = getString(R.string.user_profile_user_dob) + " " + (myProfile.getUser().getMonthOfBirth() == 0 ? "" : myProfile.getUser().getMonthOfBirth())
                    + "-" + (myProfile.getUser().getDayOfBirth() == 0 ? "" : myProfile.getUser().getDayOfBirth())
                    + "-" + (myProfile.getUser().getYearOfBirth() == 0 ? "" : myProfile.getUser().getYearOfBirth());
            dob.setText(dobString);

            String pointsString = getString(R.string.user_profile_user_points) + " " + myProfile.getUser().getRewardPoints();
            points.setText(pointsString);

            if ((requestedId == 0 || requestedId.equals(userId) ) && myProfile.getUser().getRewardPoints() > 0){
                points.setTextColor(getResources().getColor(R.color.colorPrimary));
                points.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                points.setTypeface(points.getTypeface(),Typeface.BOLD_ITALIC);
                points.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClassName("co.tagtalk.winemate",
                                "co.tagtalk.winemate.RedeemPointsActivity");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }

            followersNumber.setText(String.valueOf(myProfile.getFollowerNumber()));
            followingNumber.setText(String.valueOf(myProfile.getFollowingNumber()));
            ratingNumber.setText(String.valueOf(myProfile.getRatedNumber()));

            // If this is the my profile page, or if this profile is public, or if the user follows
            // me, then show the private contents.
            if (requestedId == 0 || requestedId.equals(userId) || !myProfile.isHideProfileToStranger() || myProfile.isIsFollowed()) {
                privateContents.setVisibility(View.VISIBLE);
            }

            String photoUrl = myProfile.getUser().getPhotoUrl();

            if (photoUrl != null && !photoUrl.isEmpty()) {
                if (refreshUserIcon) {
                    Picasso.with(this.activity).load(photoUrl)
                            .error(R.drawable.user_icon_man)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(userIcon);
                } else {
                    Picasso.with(this.activity).load(photoUrl)
                            .error(R.drawable.user_icon_man)
                            .into(userIcon);
                }
            } else {
                userIcon.setImageResource(R.drawable.user_icon_man);
            }

            // Show the relative layout content right after basic contents are filled. Others can wait.
            userProfileProgressBar.setVisibility(View.GONE);
            userProfileRelativeLayout.setVisibility(View.VISIBLE);

            final MyBottlesRequest myBottlesRequest;

            if (requestedId == 0 || requestedId.equals(userId) ) {
                // User self profile
                myBottlesRequest = new MyBottlesRequest(userId, Configs.COUNTRY_ID);
                followButton.setVisibility(GONE);
            } else {
                // Others profile
                myBottlesRequest = new MyBottlesRequest(requestedId, Configs.COUNTRY_ID);
                followButton.setVisibility(View.VISIBLE);

                if (myProfile.isFollowing) {
                    followButton.setText(getString(R.string.wine_info_activity_unfollow));
                    followButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                            String displayName = myProfile.getUser().getThirdParty() == ThirdParty.WECHAT ?
                                    myProfile.getUser().getFirstName() : myProfile.getUser().getUserName();
                            builder.setMessage(getString(R.string.wine_info_activity_unfollow_alert_message) + " " + displayName + "?")
                                    .setTitle(R.string.wine_info_activity_unfollow_alert_title);
                            builder.setPositiveButton(R.string.wine_info_activity_unfollow_alert_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final UnFollowUserTask unFollowUserTask = new UnFollowUserTask(UserProfileActivity.this, myProfile);
                                    unFollowUserTask.execute(userId, requestedId);
                                }
                            });
                            builder.setNegativeButton(R.string.wine_info_activity_unfollow_alert_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.show();
                        }
                    });
                } else {
                    followButton.setText(getString(R.string.wine_info_activity_follow));
                    followButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final FollowUserTask followUserTask = new FollowUserTask(activity, myProfile);
                            followUserTask.execute(userId, requestedId);
                        }
                    });
                }
            }

            ratingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    followersNumber.setTextColor(getResources().getColor(R.color.text_gray));
                    followersText.setTextColor(getResources().getColor(R.color.text_gray));
                    followingNumber.setTextColor(getResources().getColor(R.color.text_gray));
                    followingText.setTextColor(getResources().getColor(R.color.text_gray));
                    ratingNumber.setTextColor(getResources().getColor(R.color.text_black));
                    ratingText.setTextColor(getResources().getColor(R.color.text_black));
                    final GetMyRatingListTask getMyRatingListTask = new  GetMyRatingListTask(UserProfileActivity.this, myProfile.getUser());
                    getMyRatingListTask.execute(myBottlesRequest);
                }
            });
        }
    }

    public class GetMyFollowingListTask extends AsyncTask<FriendListRequest, Void, MyFollowingListResponse> {

        private Activity activity;
        private RecyclerView userProfileRecyclerViewFollowerList;
        private RecyclerView userProfileRecyclerViewFollowingList;
        private RecyclerView userProfileRecyclerViewRatingList;
        private UserProfileFriendListRecyclerViewAdapter userProfileFriendListRecyclerViewAdapter;
        private boolean gotException;

        public GetMyFollowingListTask(Activity activity) {
            this.activity = activity;
            userProfileRecyclerViewFollowerList = (RecyclerView) findViewById(R.id.user_profile_recycler_view_follower_list);
            userProfileRecyclerViewFollowingList = (RecyclerView) findViewById(R.id.user_profile_recycler_view_following_list);
            userProfileRecyclerViewRatingList = (RecyclerView) findViewById(R.id.user_profile_recycler_view_rating_list);

            userProfileRecyclerViewFollowingList.setVisibility(View.VISIBLE);
            userProfileRecyclerViewRatingList.setVisibility(GONE);
            userProfileRecyclerViewFollowerList.setVisibility(GONE);
            myWishlistRecyclerView.setVisibility(GONE);
        }

        @Override
        protected MyFollowingListResponse doInBackground(FriendListRequest... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            MyFollowingListResponse myFollowingListResponse = new MyFollowingListResponse();

            try{

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                myFollowingListResponse = client.getMyFollowingList(params[0]);

            } catch (TException x) {
                x.printStackTrace();
                gotException = true;
            }
            transport.close();

            return myFollowingListResponse;
        }

        @Override
        protected void onPostExecute(MyFollowingListResponse myFollowingListResponse) {
            super.onPostExecute(myFollowingListResponse);
            if (gotException) {
                Toast.makeText(this.activity, R.string.failed_to_get_remote_data, Toast.LENGTH_LONG).show();
                return;
            }

            if (Configs.DEBUG_MODE) {
                Log.v("ZZZ UserProfile: ", "myFollowingListResponse: " + myFollowingListResponse);
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false);
            userProfileRecyclerViewFollowingList.setLayoutManager(linearLayoutManager);
            userProfileFriendListRecyclerViewAdapter = new UserProfileFriendListRecyclerViewAdapter(this.activity, new ArrayList<FriendInfo>());
            userProfileRecyclerViewFollowingList.setAdapter(userProfileFriendListRecyclerViewAdapter);
            userProfileRecyclerViewFollowingList.setNestedScrollingEnabled(false);
            userProfileFriendListRecyclerViewAdapter.loadData(myFollowingListResponse.myFollowingList);

            userProfileRecyclerViewFollowingList.addOnItemTouchListener(new RecyclerViewItemClickListener(this.activity, userProfileRecyclerViewFollowingList, new RecyclerViewItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent();
                    intent.putExtra("requestedId", userProfileFriendListRecyclerViewAdapter.getUserId(position));
                    intent.setClassName("co.tagtalk.winemate",
                            "co.tagtalk.winemate.UserProfileActivity");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }));
        }
    }


    public class GetMyFollowersListTask extends AsyncTask<FriendListRequest, Void, MyFollowersListResponse> {

        private Activity activity;
        private RecyclerView userProfileRecyclerViewFollowerList;
        private RecyclerView userProfileRecyclerViewFollowingList;
        private RecyclerView userProfileRecyclerViewRatingList;
        private UserProfileFriendListRecyclerViewAdapter userProfileFriendListRecyclerViewAdapter;
        private boolean gotException;

        public GetMyFollowersListTask(Activity activity) {
            this.activity = activity;
            userProfileRecyclerViewFollowerList = (RecyclerView) findViewById(R.id.user_profile_recycler_view_follower_list);
            userProfileRecyclerViewFollowingList = (RecyclerView) findViewById(R.id.user_profile_recycler_view_following_list);
            userProfileRecyclerViewRatingList = (RecyclerView) findViewById(R.id.user_profile_recycler_view_rating_list);

            userProfileRecyclerViewFollowerList.setVisibility(View.VISIBLE);
            userProfileRecyclerViewRatingList.setVisibility(GONE);
            userProfileRecyclerViewFollowingList.setVisibility(GONE);
            myWishlistRecyclerView.setVisibility(GONE);
        }

        @Override
        protected MyFollowersListResponse doInBackground(FriendListRequest... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            MyFollowersListResponse myFollowersListResponse = new MyFollowersListResponse();

            try{

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                myFollowersListResponse = client.getMyFollowersList(params[0]);

            } catch (TException x) {
                x.printStackTrace();
                gotException = true;
            }
            transport.close();

            return myFollowersListResponse;
        }

        @Override
        protected void onPostExecute(MyFollowersListResponse myFollowersListResponse) {
            super.onPostExecute(myFollowersListResponse);

            if (gotException) {
                Toast.makeText(this.activity, R.string.failed_to_get_remote_data, Toast.LENGTH_LONG).show();
                return;
            }

            if (Configs.DEBUG_MODE) {
                Log.v("ZZZ UserProfile: ", "myFollowersListResponse: " + myFollowersListResponse);
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false);
            userProfileRecyclerViewFollowerList.setLayoutManager(linearLayoutManager);
            userProfileFriendListRecyclerViewAdapter = new UserProfileFriendListRecyclerViewAdapter(this.activity, new ArrayList<FriendInfo>());
            userProfileRecyclerViewFollowerList.setAdapter(userProfileFriendListRecyclerViewAdapter);
            userProfileRecyclerViewFollowerList.setNestedScrollingEnabled(false);
            userProfileFriendListRecyclerViewAdapter.loadData(myFollowersListResponse.myFollowersList);

            userProfileRecyclerViewFollowerList.addOnItemTouchListener(new RecyclerViewItemClickListener(this.activity, userProfileRecyclerViewFollowerList, new RecyclerViewItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent();
                    intent.putExtra("requestedId", userProfileFriendListRecyclerViewAdapter.getUserId(position));
                    intent.setClassName("co.tagtalk.winemate",
                            "co.tagtalk.winemate.UserProfileActivity");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }));
        }
    }

    public class GetMyRatingListTask extends AsyncTask<MyBottlesRequest, Void, RatedBottlesResponse> {

        private Activity activity;
        private RecyclerView userProfileRecyclerViewFollowerList;
        private RecyclerView userProfileRecyclerViewFollowingList;
        private RecyclerView userProfileRecyclerViewRatingList;
        private UserProfileRatingsRecyclerViewAdapter userProfileRatingsRecyclerViewAdapter;
        private User user;
        private boolean gotException;

        public GetMyRatingListTask(Activity activity, User user) {
            this.activity = activity;
            this.user = user;
            userProfileRecyclerViewFollowerList = (RecyclerView) findViewById(R.id.user_profile_recycler_view_follower_list);
            userProfileRecyclerViewFollowingList = (RecyclerView) findViewById(R.id.user_profile_recycler_view_following_list);
            userProfileRecyclerViewRatingList = (RecyclerView) findViewById(R.id.user_profile_recycler_view_rating_list);

            userProfileRecyclerViewRatingList.setVisibility(View.VISIBLE);
            userProfileRecyclerViewFollowerList.setVisibility(GONE);
            userProfileRecyclerViewFollowingList.setVisibility(GONE);
            myWishlistRecyclerView.setVisibility(GONE);
        }

        @Override
        protected RatedBottlesResponse doInBackground(MyBottlesRequest... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            RatedBottlesResponse myRatedBottlesResponse = new RatedBottlesResponse();

            try{

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                myRatedBottlesResponse = client.getMyRatedBottles(params[0]);

            } catch (TException x) {
                x.printStackTrace();
                gotException = true;
            }
            transport.close();

            return myRatedBottlesResponse;
        }

        @Override
        protected void onPostExecute(RatedBottlesResponse myRatedBottlesResponse) {
            super.onPostExecute(myRatedBottlesResponse);

            if (gotException) {
                Toast.makeText(this.activity, R.string.failed_to_get_remote_data, Toast.LENGTH_LONG).show();
                return;
            }

            if (Configs.DEBUG_MODE) {
                Log.v("ZZZ UserProfile: ", "myRatedBottlesResponse: " + myRatedBottlesResponse);
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false);
            userProfileRecyclerViewRatingList.setLayoutManager(linearLayoutManager);
            userProfileRatingsRecyclerViewAdapter = new UserProfileRatingsRecyclerViewAdapter(this.activity, new ArrayList<BottleInfo>(), user);
            userProfileRecyclerViewRatingList.setAdapter(userProfileRatingsRecyclerViewAdapter);
            userProfileRecyclerViewRatingList.setNestedScrollingEnabled(false);
            userProfileRatingsRecyclerViewAdapter.loadData(myRatedBottlesResponse.ratedBottleHistory);
        }
    }

    public class UnFollowUserTask extends AsyncTask<Object, Void, Void> {

        private Activity activity;
        private MyProfile mProfile;
        private boolean gotException;

        public UnFollowUserTask(Activity activity, MyProfile profile) {
            this.activity = activity;
            this.mProfile = profile;
            followButton = (Button)findViewById(R.id.user_profile_follow);
        }
        @Override
        protected Void doInBackground(Object... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);

            try{

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                Integer userId = (Integer) params[0];
                Integer userFriendId = (Integer) params[1];

                client.unfollowUser(userId, userFriendId);

            } catch (TException x) {
                x.printStackTrace();
                gotException = true;
            }
            transport.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (gotException) {
                Toast.makeText(this.activity, R.string.failed_to_do_operation, Toast.LENGTH_LONG).show();
                return;
            }

            followButton.setText(R.string.wine_info_activity_follow);
            followButton.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
            followButton.setTextColor(activity.getResources().getColor(R.color.text_white));

            followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FollowUserTask followUserTask = new FollowUserTask(activity, mProfile);
                    followUserTask.execute(userId, requestedId);
                }
            });
        }
    }


    public class FollowUserTask extends AsyncTask<Object, Void, Void> {

        private Activity activity;
        private MyProfile mProfile;
        private boolean gotException;

        public FollowUserTask(Activity activity, MyProfile profile) {
            this.activity = activity;
            followButton = (Button)findViewById(R.id.user_profile_follow);
            this.mProfile = profile;
        }
        @Override
        protected Void doInBackground(Object... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);

            try{

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                Integer userId = (Integer) params[0];
                Integer userFriendId = (Integer) params[1];

                client.followUser(userId, userFriendId);

            } catch (TException x) {
                x.printStackTrace();
                gotException = true;
            }
            transport.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (gotException) {
                Toast.makeText(this.activity, R.string.failed_to_do_operation, Toast.LENGTH_LONG).show();
                return;
            }

            followButton.setText(R.string.wine_info_activity_unfollow);
            followButton.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
            followButton.setTextColor(activity.getResources().getColor(R.color.text_white));

            followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                    String displayName = mProfile.getUser().getThirdParty() == ThirdParty.WECHAT ?
                            mProfile.getUser().getFirstName() : mProfile.getUser().getUserName();
                    builder.setMessage(getString(R.string.wine_info_activity_unfollow_alert_message) + " " + displayName + "?")
                            .setTitle(R.string.wine_info_activity_unfollow_alert_title);
                    builder.setPositiveButton(R.string.wine_info_activity_unfollow_alert_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final UnFollowUserTask unFollowUserTask = new UnFollowUserTask(UserProfileActivity.this, mProfile);
                            unFollowUserTask.execute(userId, requestedId);
                        }
                    });
                    builder.setNegativeButton(R.string.wine_info_activity_unfollow_alert_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
            });
        }
    }

    public class GetMyWishlistTask extends AsyncTask<MyBottlesRequest, Void, MyWishListResponse> {

        private Activity activity;
        private boolean gotException;

        public GetMyWishlistTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected MyWishListResponse doInBackground(MyBottlesRequest... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            MyWishListResponse myWishListResponse = new MyWishListResponse();

            try {

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                myWishListResponse = client.getMyWishlist(params[0]);

            } catch (TException x) {
                x.printStackTrace();
                gotException = true;
            }
            transport.close();

            return myWishListResponse;
        }

        @Override
        protected void onPostExecute(MyWishListResponse myWishListResponse) {
            super.onPostExecute(myWishListResponse);

            if (gotException) {
                Toast.makeText(this.activity, R.string.failed_to_get_remote_data, Toast.LENGTH_LONG).show();
                return;
            }

            if (!myWishListResponse.success) {
                Log.v("getMyWishList: ", "service call returned with error!");
            }
            if (Configs.DEBUG_MODE) {
                Log.v("Arthur - My Wishlist: ", "list of wishlist: " + myWishListResponse.wishList);
                if (myWishListResponse.wishList != null) {
                    Log.v("Arthur - My Wishlist: ", "list of wishlist size: " + myWishListResponse.wishList.size());
                }
            }
            if (myWishListResponse.wishList != null) {
                myWishlistRecyclerViewAdapter.loadData(myWishListResponse.wishList);
            }
        }
    }
}
