package co.tagtalk.winemate;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.util.List;

import co.tagtalk.winemate.thriftfiles.AddToWishlistRequest;
import co.tagtalk.winemate.thriftfiles.BottleInfo;
import co.tagtalk.winemate.thriftfiles.WineMateServices;

import static android.view.View.GONE;

/**
 * Created by Zhaoyu on 2016/7/23.
 */
public class MyBottlesRecyclerViewAdapter extends RecyclerView.Adapter <MyBottlesRecyclerViewAdapter.MyBottlesRecyclerViewHolder> {
    private List<BottleInfo> mBottleHistory;
    private Context mContext;
    private boolean showDeleteButton;

    public MyBottlesRecyclerViewAdapter(Context context, List<BottleInfo> bottleInfoList) {
        mContext = context;
        this.mBottleHistory = bottleInfoList;
        this.showDeleteButton = false;
    }

    public MyBottlesRecyclerViewAdapter(Context context, List<BottleInfo> bottleInfoList, boolean showDeleteButton) {
        mContext = context;
        this.mBottleHistory = bottleInfoList;
        this.showDeleteButton = showDeleteButton;
    }

    @Override
    public MyBottlesRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_bottles_single_item, parent, false);
        return new MyBottlesRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyBottlesRecyclerViewHolder holder, int position) {
        final BottleInfo openedBottleInfo = mBottleHistory.get(position);

        if (openedBottleInfo.getWinePicUrl() != null && openedBottleInfo.getWinePicUrl().length() > 0) {
            Picasso.with(mContext).load(openedBottleInfo.getWinePicUrl())
                    .error(R.drawable.placeholder)
                    .into(holder.winePicture);

        }

        if (openedBottleInfo.getNationalFlagUrl() != null && openedBottleInfo.getNationalFlagUrl().length() > 0) {
            Picasso.with(mContext).load(openedBottleInfo.getNationalFlagUrl())
                    .error(R.drawable.placeholder)
                    .into(holder.nationalFlag);
        }

        holder.openDate.setText(openedBottleInfo.getOpenDate());
        holder.wineName.setText("Tamburlaine "+ openedBottleInfo.getWineName() + " " + openedBottleInfo.getYear());
        holder.regionName.setText(openedBottleInfo.getRegionName());
        holder.openCity.setText(openedBottleInfo.getOpenCity());
        holder.openTime.setText(openedBottleInfo.getOpenTime());

        if (openedBottleInfo.getOpenCity() == null) {
            holder.openCityIcon.setVisibility(GONE);
        }

        if (openedBottleInfo.getOpenTime() == null) {
            holder.openTimeIcon.setVisibility(GONE);
        }

        holder.myBottleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("wineId", openedBottleInfo.getWineId());
                intent.putExtra("winePicURL", openedBottleInfo.getWinePicUrl());
                intent.putExtra("isSealed", false);
                intent.setClassName("co.tagtalk.winemate",
                        "co.tagtalk.winemate.WineInfoActivity");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });

        // This is for Wish List item only.
        // TODO(Arthur): create a seperate RecyclerViewAdapter class for Wish List, and remove this from MyBottlesRecyclerViewAdapter.
        if (showDeleteButton) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.bringToFront();
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(R.string.wishlist_remove_alert_message).setTitle(R.string.wishlist_remove_alert_title);
                    builder.setPositiveButton(R.string.wishlist_remove_alert_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int userId;
                            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                            if (sharedPrefs.getInt(Configs.USER_ID, 0) != 0 && sharedPrefs.getBoolean(Configs.LOGIN_STATUS, false)) {
                                userId = sharedPrefs.getInt(Configs.USER_ID, 0);
                            } else {
                                userId = Configs.userId;
                            }
                            AddToWishlistRequest addToWishlistRequest = new AddToWishlistRequest(userId, openedBottleInfo.getWineId(), false);
                            final DeleteFromWishlistTask addToWishlistTask = new DeleteFromWishlistTask((Activity)mContext);
                            addToWishlistTask.execute(addToWishlistRequest);
                        }
                    });
                    builder.setNegativeButton(R.string.wine_info_activity_unfollow_alert_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                    builder.show();
                }
            });
        }
    }

    // TODO(Arthur): create a seperate RecyclerViewAdapter class for Wish List, and remove this from MyBottlesRecyclerViewAdapter.
    public class DeleteFromWishlistTask extends AsyncTask<AddToWishlistRequest, Void, Boolean> {

        private Activity activity;
        private boolean gotException;

        public DeleteFromWishlistTask(Activity activity) {
            this.activity = activity;
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
                gotException = true;
            }
            transport.close();

            return response;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (gotException || !success) {
                Toast.makeText(activity, mContext.getString(R.string.failed_to_remove_from_wishlist_text), Toast.LENGTH_SHORT).show();
            } else {
                // Refresh wish list.
                final RelativeLayout wishlistView = (RelativeLayout)((Activity)mContext).findViewById(R.id.user_profile_wishlist_view);
                wishlistView.callOnClick();
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != mBottleHistory ? mBottleHistory.size() : 0);
    }

    public void loadData(List<BottleInfo> bottleInfoList) {
        mBottleHistory = bottleInfoList;
        notifyDataSetChanged();
    }

    public int getWineId(int position) {
        return null != mBottleHistory ? mBottleHistory.get(position).getWineId(): 0;
    }

    public String getWinePicUrl(int position) {
        return null != mBottleHistory ? mBottleHistory.get(position).getWinePicUrl(): null;
    }

    public class MyBottlesRecyclerViewHolder extends RecyclerView.ViewHolder {
        protected RelativeLayout myBottleItem;
        protected ImageView winePicture;
        protected ImageView nationalFlag;
        protected ImageView openTimeIcon;
        protected ImageView openCityIcon;
        protected TextView openDate;
        protected TextView wineName;
        protected TextView regionName;
        protected TextView openCity;
        protected TextView openTime;
        protected ImageButton deleteButton;

        public MyBottlesRecyclerViewHolder(View view) {
            super(view);
            this.myBottleItem = (RelativeLayout) view;
            this.winePicture = (ImageView) view.findViewById(R.id.my_bottles_single_item_wine_picture);
            this.nationalFlag = (ImageView) view.findViewById(R.id.my_bottles_single_item_national_flag);
            this.openTimeIcon = (ImageView) view.findViewById(R.id.my_bottles_single_item_open_time_icon);
            this.openCityIcon = (ImageView) view.findViewById(R.id.my_bottles_single_item_open_city_icon);
            this.openDate = (TextView) view.findViewById(R.id.my_bottles_single_item_open_date);
            this.wineName = (TextView) view.findViewById(R.id.my_bottles_single_item_wine_name);
            this.regionName = (TextView) view.findViewById(R.id.my_bottles_single_item_wine_region);
            this.openCity = (TextView) view.findViewById(R.id.my_bottles_single_item_open_city);
            this.openTime = (TextView) view.findViewById(R.id.my_bottles_single_item_open_time);
            this.deleteButton = (ImageButton) view.findViewById(R.id.my_bottles_single_item_delete);
        }
    }
}

