package co.tagtalk.winemate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.tagtalk.winemate.thriftfiles.RewardRedeemSingleItem;
import co.tagtalk.winemate.thriftfiles.RewardSingleItem;

/**
 * Created by Zhaoyu on 2017/1/19.
 */

public class RedeemPointsRecyclerViewAdapter extends RecyclerView.Adapter<RedeemPointsRecyclerViewAdapter.RedeemPointsRecyclerViewHolder> {

    private List<RewardSingleItem> rewardItemList;
    private List<RewardRedeemSingleItem> rewardRedeemList;
    private Context mContext;

    public RedeemPointsRecyclerViewAdapter(Context context, List<RewardSingleItem> rewardItemList, List<RewardRedeemSingleItem> rewardRedeemList) {
        this.mContext = context;
        this.rewardItemList = rewardItemList;
        this.rewardRedeemList = rewardRedeemList;
    }

    @Override
    public RedeemPointsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.redeem_single_item, parent, false);
        return new RedeemPointsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RedeemPointsRecyclerViewHolder holder, int position) {
        holder.wineName.setText(rewardItemList.get(position).getWineName());
        holder.wineYear.setText(rewardItemList.get(position).getYear());
        holder.wineRegion.setText(rewardItemList.get(position).getRegion());
        holder.winePoints.setText(mContext.getString(R.string.redeem_points_activity_points) + " " + rewardItemList.get(position).getPoints());

        Picasso.with(mContext).load(rewardItemList.get(position).getWinePicUrl())
                .error(R.drawable.placeholder)
                .into(holder.winePic);

        if (rewardItemList.get(position).outOfStock) {
            holder.wineOutOfStock.setVisibility(View.VISIBLE);
            holder.itemSelectLayout.setVisibility(View.GONE);
        } else {
            holder.wineOutOfStock.setVisibility(View.GONE);
            holder.itemSelectLayout.setVisibility(View.VISIBLE);

            holder.itemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    RewardRedeemSingleItem item;

                    if (compoundButton.isChecked()) {
                        if (holder.quantityInput.getText().toString().length() > 0 && Integer.parseInt(holder.quantityInput.getText().toString()) > 0) {
                            item = new RewardRedeemSingleItem(rewardItemList.get(holder.getAdapterPosition()).getWineId(), Integer.parseInt(holder.quantityInput.getText().toString()), rewardItemList.get(holder.getAdapterPosition()).getPoints());
                            compoundButton.setChecked(true);
                            rewardRedeemList.add(item);
                            holder.quantityInput.setFocusable(false);
                            holder.quantityInput.setClickable(false);
                            holder.quantityInput.setFocusableInTouchMode(false);
                        } else {
                            compoundButton.setChecked(false);
                            Toast.makeText(mContext, mContext.getString(R.string.redeem_points_activity_warning_enter_number), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (holder.quantityInput.getText().toString().length() > 0 && Integer.parseInt(holder.quantityInput.getText().toString()) > 0) {
                            item = new RewardRedeemSingleItem(rewardItemList.get(holder.getAdapterPosition()).getWineId(), Integer.parseInt(holder.quantityInput.getText().toString()), rewardItemList.get(holder.getAdapterPosition()).getPoints());

                            compoundButton.setChecked(false);
                            if (rewardRedeemList.contains(item)) {
                                rewardRedeemList.remove(item);
                            }
                            holder.quantityInput.setFocusable(true);
                            holder.quantityInput.setClickable(true);
                            holder.quantityInput.setFocusableInTouchMode(true);
                            holder.quantityInput.setText("");
                        }
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return (null != rewardItemList ? rewardItemList.size() : 0);
    }

    public void loadData(List<RewardSingleItem> rewardItemList) {
        this.rewardItemList = rewardItemList;
        notifyDataSetChanged();
    }

    public class RedeemPointsRecyclerViewHolder extends RecyclerView.ViewHolder {
        protected ImageView winePic;
        protected TextView wineName;
        protected TextView wineYear;
        protected TextView wineRegion;
        protected TextView winePoints;
        protected TextView wineOutOfStock;
        protected LinearLayout itemSelectLayout;
        protected CheckBox itemCheckBox;
        protected EditText quantityInput;

        public RedeemPointsRecyclerViewHolder(View view) {
            super(view);
            this.winePic = (ImageView) view.findViewById(R.id.redeem_single_item_wine_pic);
            this.wineName = (TextView) view.findViewById(R.id.redeem_single_item_wine_name);
            this.wineYear = (TextView) view.findViewById(R.id.redeem_single_item_wine_year);
            this.wineRegion = (TextView) view.findViewById(R.id.redeem_single_item_wine_region);
            this.winePoints = (TextView) view.findViewById(R.id.redeem_single_item_wine_points);
            this.wineOutOfStock = (TextView) view.findViewById(R.id.redeem_single_item_out_of_stock);
            this.itemSelectLayout = (LinearLayout) view.findViewById(R.id.redeem_single_item_select);
            this.itemCheckBox = (CheckBox) view.findViewById(R.id.redeem_single_item_check_box);
            this.quantityInput = (EditText) view.findViewById(R.id.redeem_single_item_quantity_input);
        }
    }
}
