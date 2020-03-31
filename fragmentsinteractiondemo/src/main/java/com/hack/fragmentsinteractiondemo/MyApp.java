package com.hack.fragmentsinteractiondemo;

import android.app.Application;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MyApp extends Application {
    public static final String MARKETS_URL = "https://api.bittrex.com/api/v1.1/public/getmarketsummaries";
    TextView tv_dataFromMarkets;

    public ArrayList<User> getData(){
        String[] names = this.getResources().getStringArray(R.array.data);
        ArrayList<User> data = new ArrayList<>();
        Random rnd = new Random();
        for(int i=0;i<names.length;++i){
            data.add(new User(names[i],i+1,rnd.nextBoolean()));
        }
        return data;
    }

}
