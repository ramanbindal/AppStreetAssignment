package com.example.presentation.ui.main;

import com.example.domain.model.ImageData;

import java.util.List;

public interface MainNavigator {


    void onListLoaded(List<ImageData> imageData);

    void noData();

    void dataLoadedFromDb();

}
