package com.example.presentation.ui.di;

import android.arch.lifecycle.ViewModelProvider;

import com.example.domain.usecases.FetchPhotosUseCase;
import com.example.domain.usecases.GetImagesFromDb;
import com.example.presentation.base.ViewModelProviderFactory;
import com.example.presentation.ui.firstFragment.GridFragmentViewModel;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {


    @Provides
    GridFragmentViewModel provideGridFragmentViewModel(FetchPhotosUseCase fetchPhotosUseCase, GetImagesFromDb getImagesFromDb) {
        return new GridFragmentViewModel(fetchPhotosUseCase,getImagesFromDb);
    }

    @Provides
    @Named("GridFragment")
    ViewModelProvider.Factory gridFragmentViewModelProvider(GridFragmentViewModel mainViewModel) {
        return new ViewModelProviderFactory<>(mainViewModel);
    }

   }

