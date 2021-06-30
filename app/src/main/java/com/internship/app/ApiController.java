package com.internship.app;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController {

    private static ApiController apiController;
    private static Retrofit retrofit;

    ApiController() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ConstantUrl.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ApiController getInstance(){
        if (apiController==null){
            apiController = new ApiController();
        }
        return apiController;
    }

    ApiSet getapi(){
        return retrofit.create(ApiSet.class);
    }
}
