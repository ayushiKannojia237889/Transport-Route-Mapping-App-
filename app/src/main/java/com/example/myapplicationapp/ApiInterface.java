package com.example.myapplicationapp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

   // get  state data
    @GET("getState")
    Call<List<State>> getState();


 // get district data

    @GET("getDistrict/{sid}")
    Call<List<District>> getDistrict(@Path("sid") int sid);

    // post data
    @POST("employees")
    Call<Model> addData(@Body Model model);







}
