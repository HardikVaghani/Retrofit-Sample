package com.hardik.retrofitsample.api;

import static com.hardik.retrofitsample.utils.Constants.BASE_URL;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static String TAG = RetrofitInstance.class.getSimpleName();
    /// private String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static RetrofitInstance instance = null;
    public static ApiInterface apiInterface;

    private RetrofitInstance() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    request = request.newBuilder()
//                            .addHeader("Authorization", "Bearer " + BEARER_TOKEN_AUTHORIZATION)
                            .build();
                    return chain.proceed(request);
                })
                .callTimeout(90, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public static synchronized RetrofitInstance getInstance() {
        Log.d(TAG, "getInstance: ");
        if (instance == null){
            instance = new RetrofitInstance();
        }
        return instance;
    }

}
