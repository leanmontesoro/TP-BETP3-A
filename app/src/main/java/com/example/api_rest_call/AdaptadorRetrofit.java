package com.example.api_rest_call;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdaptadorRetrofit {

    Retrofit retrofit;

    public AdaptadorRetrofit() {
    }

    public Retrofit getAdaptador(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-central1-be-tp3-a.cloudfunctions.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;

    }


}
