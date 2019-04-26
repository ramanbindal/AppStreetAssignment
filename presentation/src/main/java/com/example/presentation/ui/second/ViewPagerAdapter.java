package com.example.presentation.ui.second;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.domain.model.ImageData;
import com.example.presentation.R;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<ImageData> data;

    public ViewPagerAdapter(Context context, List<ImageData> data) {
        mContext = context;
        this.data = data;
    }

    @Override
    public Object instantiateItem(final ViewGroup collection, final int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.view_pager_layout, collection, false);

        ImageView imageView = (ImageView) layout.findViewById(R.id.view_pager_iv);


        byte[] decodedString = Base64.decode(data.get(position).getImageBas64(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        Glide.with(mContext)
//                .load(decodedString)
//                .into(imageView);

//        imageView.setImageBitmap(data.get(position));

        imageView.setImageBitmap(decodedByte);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }


}

