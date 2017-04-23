package co.tagtalk.winemate;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.tagtalk.winemate.thriftfiles.BottleInfo;
import co.tagtalk.winemate.thriftfiles.ThirdParty;
import co.tagtalk.winemate.thriftfiles.User;

/**
 * Created by Zhaoyu on 2016/11/19.
 */

public class UserProfileRatingsRecyclerViewAdapter extends RecyclerView.Adapter <UserProfileRatingsRecyclerViewAdapter.UserProfileRatingsRecyclerViewHolder> {

    private List<BottleInfo> mRatedBottleHistory;
    private Context mContext;
    private User user;

    private int userId;

    public UserProfileRatingsRecyclerViewAdapter(Context context, List<BottleInfo> bottleInfoList, User user) {
        this.mContext = context;
        this.mRatedBottleHistory = bottleInfoList;
        this.user = user;

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        if (sharedPrefs.getInt(Configs.USER_ID, 0) != 0 && sharedPrefs.getBoolean(Configs.LOGIN_STATUS, false)) {
            userId = sharedPrefs.getInt(Configs.USER_ID, 0);
        } else {
            userId = Configs.userId;;
        }
    }


    @Override
    public UserProfileRatingsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wine_reviews, parent, false);
        return new UserProfileRatingsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserProfileRatingsRecyclerViewHolder holder, int position) {
        final BottleInfo ratedBottleInfo = mRatedBottleHistory.get(position);

        String photoUrl = user.getPhotoUrl();

        if (photoUrl != null && !photoUrl.isEmpty()) {
            Picasso.with(this.mContext).load(photoUrl)
                    .error(R.drawable.user_icon_man)
                    .into(holder.reviewerIcon);

        } else {
            holder.reviewerIcon.setImageResource(R.drawable.user_icon_man);
        }

        // If the use is a wechat user, show nickname here.
        if (user.getThirdParty() == ThirdParty.WECHAT) {
            holder.reviewerName.setText(user.getFirstName());
        } else {
            holder.reviewerName.setText(user.getUserName());
        }
        holder.reviewTimeStamp.setText(ratedBottleInfo.getOpenDate());
        holder.reviewerRatingBar.setRating((float) ratedBottleInfo.getMyRate());
        holder.follow.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return (null != mRatedBottleHistory ? mRatedBottleHistory.size() : 0);
    }


    public void loadData(List<BottleInfo> bottleInfoList) {
        mRatedBottleHistory = bottleInfoList;
        notifyDataSetChanged();
    }

    public class UserProfileRatingsRecyclerViewHolder extends RecyclerView.ViewHolder{
        protected ImageView reviewerIcon;
        protected TextView reviewerName;
        protected TextView reviewTimeStamp;
        protected RatingBar reviewerRatingBar;
        protected TextView  ratedIt;
        protected Button follow;

        public UserProfileRatingsRecyclerViewHolder(View view) {
            super(view);
            this.reviewerIcon = (ImageView) view.findViewById(R.id.wine_info_reviewer_icon);
            this.reviewerName = (TextView) view.findViewById(R.id.wine_info_reviewer_name);
            this.reviewTimeStamp = (TextView) view.findViewById(R.id.wine_info_review_time_elapsed);
            this.reviewerRatingBar = (RatingBar) view.findViewById(R.id.wine_info_reviewer_rating_bar_indicator);
            this.ratedIt = (TextView) view.findViewById(R.id.wine_info_rated_it);
            this.follow = (Button) view.findViewById(R.id.wine_info_review_follow);
        }

    }

}
