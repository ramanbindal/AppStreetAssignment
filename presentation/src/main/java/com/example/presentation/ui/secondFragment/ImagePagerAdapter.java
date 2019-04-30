/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.presentation.ui.secondFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import static com.google.samples.gridtopager.adapter.ImageData.IMAGE_DRAWABLES;

public class ImagePagerAdapter extends FragmentStatePagerAdapter {

  public ImagePagerAdapter(Fragment fragment) {
    // Note: Initialize with the child fragment manager.
    super(fragment.getChildFragmentManager());
  }

  @Override
  public int getCount() {
    return 2;
  }

  @Override
  public Fragment getItem(int position) {
    return ImageFragment.newInstance(IMAGE_DRAWABLES[position]);
  }
}