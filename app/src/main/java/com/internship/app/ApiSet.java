package com.internship.app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiSet {

    @FormUrlEncoded
    @POST("signUp.php")
    Call<SignUpResponseModel> signUp(
            @Field("name") String name,
            @Field("email") String email,
            @Field("contact") String contact,
            @Field("password") String password,
            @Field("gender") String gender
    );
    
    @GET("login.php")
    Call<LoginResponseModel> login(
            @Query("email") String email,
            @Query("password") String password
    );

    @FormUrlEncoded
    @POST("update.php")
    Call<SignUpResponseModel> update(
            @Field("id") String id,
            @Field("email") String email,
            @Field("name") String name,
            @Field("contact") String contact,
            @Field("password") String password
    );

    @GET("delete.php")
    Call<SignUpResponseModel> delete(
            @Query("id") String id
    );

    @GET("categories.php")
    Call<List<CategoriesResponseModel>> getCategories();

    @GET("sub_categories.php")
    Call<List<CategoriesResponseModel>> getSubCategories(
            @Query("id") int cat_id
    );
}
//http_response_code(404);