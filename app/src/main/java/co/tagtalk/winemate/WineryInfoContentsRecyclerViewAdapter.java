package co.tagtalk.winemate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import co.tagtalk.winemate.thriftfiles.WineryInfoSingleContent;

import java.util.List;

/**
 * Created by Zhaoyu on 2016/12/31.
 */

public class WineryInfoContentsRecyclerViewAdapter extends RecyclerView.Adapter<WineryInfoContentsRecyclerViewAdapter.WineryInfoContentsRecyclerViewHolder>  {

    private List<WineryInfoSingleContent> wineryInfoContentList;
    private Context mContext;

    public WineryInfoContentsRecyclerViewAdapter(Context context, List<WineryInfoSingleContent> contentList) {
        this.mContext = context;
        this.wineryInfoContentList = contentList;
    }

    @Override
    public WineryInfoContentsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.winery_info_single_content, parent, false);
        return new WineryInfoContentsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WineryInfoContentsRecyclerViewHolder holder, final int position) {
        holder.contentTitle.setText(wineryInfoContentList.get(position).getTitle());
        holder.contentText.setText(wineryInfoContentList.get(position).getBriefText());
        holder.contentViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("wineryInfoDetailUrl", wineryInfoContentList.get(holder.getAdapterPosition()).getUrl());
                intent.setClassName("co.tagtalk.winemate",
                        "co.tagtalk.winemate.WineryInfoDetailWebViewActivity");
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != wineryInfoContentList ? wineryInfoContentList.size() : 0);
    }

    public void loadData(List<WineryInfoSingleContent> contentList) {
        this.wineryInfoContentList = contentList;
        notifyDataSetChanged();
    }

    public class WineryInfoContentsRecyclerViewHolder extends RecyclerView.ViewHolder{
        protected TextView contentTitle;
        protected TextView contentText;
        protected Button contentViewMore;

        public WineryInfoContentsRecyclerViewHolder(View view) {
            super(view);
            this.contentTitle = (TextView) view.findViewById(R.id.winery_info_content_title);
            this.contentText = (TextView) view.findViewById(R.id.winery_info_content_text);
            this.contentViewMore = (Button) view.findViewById(R.id.winery_info_content_view_more);
        }
    }
}
