package com.hardik.retrofitsample;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.hardik.retrofitsample.databinding.ActivityMainBinding;
import com.hardik.retrofitsample.repository.RepositoryInstance;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    public MainViewModel viewModel;
//    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // RepositoryInstance,ViewModelProviderFactory,ViewModel
        RepositoryInstance repositoryInstance = new RepositoryInstance();
        MainViewModelProviderFactory viewModelProviderFactory = new MainViewModelProviderFactory(getApplication(), repositoryInstance);
        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel.class);
//        viewModel.getPhotos();


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
//        progressBar = binding.progressBar;

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });

//        viewModel.getPhotos();
//        viewModel.photos.observe(this, photoResponseResource -> {
//            if (photoResponseResource != null && photoResponseResource.getData() != null) {
                /*for (PhotoResponseItem item : photoResponseResource.getData()) {
                    // Use 'item' here to access each individual item
                    Log.d(TAG, "Item id: " + item.getId());
                    Log.d(TAG, "Item title: " + item.getTitle());
                }*/
        /// OR
                /*Iterator<PhotoResponseItem> iterator = photoResponseResource.getData().iterator();
                while (iterator.hasNext()) {
                    PhotoResponseItem item = iterator.next();
                    // Use 'item' here to access each individual item
                    Log.w(TAG, "Item id: " + item.getId());
                    Log.w(TAG, "Item title: " + item.getTitle());
                }*/
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}