package com.example.presentation.di;


import com.example.presentation.ui.di.MainModule;
import com.example.presentation.ui.firstFragment.GridFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, UseCaseModule.class, RepositoryModule.class, MainModule.class})
public interface MyComponent {

    void inject(GridFragment gridFragment);

}
