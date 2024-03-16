package com.hardik.retrofitsample;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.hardik.retrofitsample.models.DataResponse;
import com.hardik.retrofitsample.models.DataResponseItem;
import com.hardik.retrofitsample.repository.RepositoryInstance;
import com.hardik.retrofitsample.utils.Resource;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {
    private final String TAG;
    private final RepositoryInstance repositoryInstance;

    public MainViewModel(Application app, RepositoryInstance repositoryInstance) {
        super(app);
        this.TAG = MainViewModel.class.getSimpleName();
        this.repositoryInstance = repositoryInstance;
    }

    public MutableLiveData<Resource<DataResponse>> posts = new MutableLiveData<>();
    private DataResponse dataResponse = null;

    public void getData() {
        new Thread(() -> {
//        viewModelScope.launch(() -> {
            Log.d(TAG, "getPhotos: viewModelScope(no visibility) is not thread safe");
            safeDataCall();
//        });
        }).start();
    }

    private void safeDataCall() {
        posts.postValue(new Resource.Loading<>());
        try {
            if (hasInternetConnection()) {
                Response<List<DataResponseItem>> response = repositoryInstance.getPosts().execute();
                posts.postValue(handleDataResponse(response));
            } else {
                posts.postValue(new Resource.Error<>("NoInternetConnection"));
            }
        } catch (IOException e) {
            Log.d(TAG, "safePhotoCall: " + e.getLocalizedMessage());
            posts.postValue(new Resource.Error<>(e.getLocalizedMessage()));
        }
    }

    private Resource<DataResponse> handleDataResponse(Response<List<DataResponseItem>> response) {
        if (response.isSuccessful()) {
            List<DataResponseItem> resultResponseList = response.body();
            if (resultResponseList != null) {
                if (dataResponse == null) {
                    dataResponse = new DataResponse();
                }
                dataResponse.addAll(resultResponseList);
                return new Resource.Success<>(dataResponse);
            } else {
                return new Resource.Error<>("Response body is null");
            }
        } else {
            return new Resource.Error<>(response.message());
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private boolean hasInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        } else {
            assert connectivityManager.getActiveNetworkInfo() != null;
            return connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI
                    || connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE
                    || connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_ETHERNET;
        }
    }
}
