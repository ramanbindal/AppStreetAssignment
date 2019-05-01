package com.example.presentation.ui.firstFragment;

import com.example.domain.model.ImageData;

import java.util.List;

public interface GridFragmentNavigator {
    void onListLoaded(List<ImageData> imageData);

    void noData();

    void dataLoadedFromDb();
}
