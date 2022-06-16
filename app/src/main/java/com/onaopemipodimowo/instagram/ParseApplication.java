package com.onaopemipodimowo.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        ParseObject.registerSubclass(Post.class);


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("a60A7cGf5Ddejs0NeUceyAUGeNtWdnhczds3P90X")
                .clientKey("TwGHLWbwTmkDD6UAcJE5nm4pawE2PPNaqgjLjUoc")
                .server("https://parseapi.back4app.com")
                .build()
        );


    }
}
