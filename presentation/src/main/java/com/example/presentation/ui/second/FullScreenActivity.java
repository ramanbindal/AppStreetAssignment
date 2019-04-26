package com.example.presentation.ui.second;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Toast;

import com.example.domain.model.ImageData;
import com.example.presentation.R;
import com.example.presentation.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class FullScreenActivity extends AppCompatActivity {

    private ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    public static List<ImageData> imageDataList=new ArrayList<>() ;
    int position = 0;

    public List<ImageData> getImageDataList() {
        return imageDataList;
    }

    public void setImageDataList(List<ImageData> imageDataList) {
        this.imageDataList = imageDataList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        viewPager = findViewById(R.id.view_pager);


        if (getIntent() != null&&getIntent().getExtras()!=null) {
//            imageDataList = (List<ImageData>) getIntent().getExtras().getSerializable("LIST");
            position = getIntent().getIntExtra("position", 0);
        }
        Toast.makeText(this, ""+imageDataList.toString(), Toast.LENGTH_SHORT).show();


//        List<Bitmap> bitmapList=new ArrayList<>();
//        for(ImageData imageData:imageDataList)
//        {
//            byte[] decodedString = Base64.decode(imageData.getImageBas64(), Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            bitmapList.add(decodedByte);
//        }

        viewPagerAdapter = new ViewPagerAdapter(this, imageDataList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(position);
    }
}
