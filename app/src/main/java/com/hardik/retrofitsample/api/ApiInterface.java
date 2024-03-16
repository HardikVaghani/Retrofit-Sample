package com.hardik.retrofitsample.api;

import com.hardik.retrofitsample.models.DataResponseItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("/posts")
    Call<List<DataResponseItem>> getPosts();
}
