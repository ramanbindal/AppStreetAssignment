package com.example.presentation.ui.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.domain.model.ImageData;

import java.util.List;

public class GridLayoutAdapter extends BaseAdapter {

    List<ImageData> imageDataList;
    Context context;

    public GridLayoutAdapter(List<ImageData> imageDataList, Context context) {
        this.imageDataList = imageDataList;
        this.context = context;
    }

    public void setImageDataList(List<ImageData> imageDataList) {
        this.imageDataList = imageDataList;
    }

    @Override
    public int getCount() {
        return imageDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageDataList.get(position).getImageBas64();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);

        byte[] decodedString = Base64.decode(imageDataList.get(position).getImageBas64(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


        imageView.setImageBitmap(decodedByte);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(300,300));
        return imageView;
    }

}
