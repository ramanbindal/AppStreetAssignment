
package com.example.presentation.ui.secondFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.domain.model.ImageData;

import java.util.List;


public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    List<ImageData> imageDataList;

    public ImagePagerAdapter(Fragment fragment, List<ImageData> imageDataList) {
        super(fragment.getChildFragmentManager());
        this.imageDataList = imageDataList;
    }

    @Override
    public int getCount() {
        return imageDataList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(imageDataList.get(position).getImageBas64());
    }
}
