package com.example.presentation.ui.di;

import android.arch.lifecycle.ViewModelProvider;

import com.example.presentation.base.ViewModelProviderFactory;
import com.example.presentation.ui.main.MainViewModel;
import com.example.domain.usecases.GetSum;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Provides
    MainViewModel provideMainViewModel(GetSum getSumUseCase) {
        return new MainViewModel(getSumUseCase);
    }

    @Provides
    ViewModelProvider.Factory mainViewModelProvider(MainViewModel mainViewModel) {
        return new ViewModelProviderFactory<>(mainViewModel);
    }

   }

