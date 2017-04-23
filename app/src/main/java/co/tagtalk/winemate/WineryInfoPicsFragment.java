package co.tagtalk.winemate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Zhaoyu on 2016/12/28.
 */

public class WineryInfoPicsFragment extends Fragment {

    private ImageView wineryPics;
    private static final String WINERY_PHOTO_URL = "url";

    public static WineryInfoPicsFragment newInstance(String wineryPhotoUrl) {
        Bundle args = new Bundle();
        args.putString(WINERY_PHOTO_URL, wineryPhotoUrl);

        WineryInfoPicsFragment wineryInfoPicsFragment = new WineryInfoPicsFragment();
        wineryInfoPicsFragment.setArguments(args);
        return wineryInfoPicsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.winery_pic_single_item, container, false);
        wineryPics = (ImageView) view.findViewById(R.id.winery_pic_single_picture);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        String wineryPhotoUrl = getArguments().getString(WINERY_PHOTO_URL);
        Picasso.with(this.getActivity()).load(wineryPhotoUrl)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(wineryPics);

        Log.v("XXX", "winery pics: " + wineryPhotoUrl);
    }
}
