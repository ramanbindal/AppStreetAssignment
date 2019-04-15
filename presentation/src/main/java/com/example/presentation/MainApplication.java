package com.example.presentation;

import android.app.Application;

import com.example.presentation.di.AppModule;
import com.example.presentation.di.DaggerMyComponent;
import com.example.presentation.di.MyComponent;

public class MainApplication extends Application {

    private MyComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerMyComponent.builder().appModule(new AppModule(getApplicationContext())).build();
    }

    public MyComponent getComponent() {
        return this.component;
    }
}
