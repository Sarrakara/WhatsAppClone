package com.example.whatsappclone;

import android.app.Application;

import com.parse.Parse;

import java.text.ParseException;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();



        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("WG9DZpaFohgo53ZJeO8zqRhkig9F4UHBOZ3elI7y")
                // if defined
                .clientKey("ZjvmALOmdV8zVQRwqGdoao2kHagBgeO0wkeczhkd")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
