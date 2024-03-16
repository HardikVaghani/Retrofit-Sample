package com.hardik.retrofitsample;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hardik.retrofitsample.repository.RepositoryInstance;

public class MainViewModelProviderFactory implements ViewModelProvider.Factory {
    private final Application app;
    private final RepositoryInstance repositoryInstance;
    private final String TAG;

    public MainViewModelProviderFactory(Application app, RepositoryInstance repositoryInstance) {
        this.app = app;
        this.repositoryInstance = repositoryInstance;
        this.TAG = MainViewModelProviderFactory.class.getSimpleName();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Log.d(TAG, "create: ");
        return (T) new MainViewModel(app, repositoryInstance);
    }
}

