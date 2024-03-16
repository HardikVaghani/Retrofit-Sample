package com.hardik.retrofitsample.repository;


import com.hardik.retrofitsample.api.RetrofitInstance;
import com.hardik.retrofitsample.models.DataResponseItem;

public class RepositoryInstance {

    public RepositoryInstance() {
        RetrofitInstance.getInstance();
    }

    public retrofit2.Call<java.util.List<DataResponseItem>> getPosts() {
        return RetrofitInstance.apiInterface.getPosts();
    }
}
