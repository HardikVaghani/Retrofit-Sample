package com.hardik.retrofitsample.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hardik.retrofitsample.MainActivity;
import com.hardik.retrofitsample.MainViewModel;
import com.hardik.retrofitsample.R;
import com.hardik.retrofitsample.adapter.DataAdapter;
import com.hardik.retrofitsample.databinding.FragmentFirstBinding;
import com.hardik.retrofitsample.models.DataResponseItem;
import com.hardik.retrofitsample.utils.Resource;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {
    private String TAG = FirstFragment.class.getSimpleName();

    private FragmentFirstBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MainViewModel viewModel;
    private DataAdapter adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.d(TAG, "onCreateView: ");

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

//        progressBar = binding.progressBar;
        recyclerView = binding.recyclerView;
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);


        // Initialize ViewModel
        if (requireContext() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) requireContext();
            // Now you can access public methods or variables of MainActivity
            viewModel = mainActivity.viewModel;
            // Use viewModel as needed
        }

        adapter = new DataAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");

        viewModel.getData();
        viewModel.posts.observe(getViewLifecycleOwner(), photoResponseResource -> {

            if (photoResponseResource instanceof Resource.Success) {
                hideProgressBar();
                Object data = ((Resource.Success<?>) photoResponseResource).getData();
                if (data != null) {
                    adapter.updateData((List<DataResponseItem>) data);
                }
            } else if (photoResponseResource instanceof Resource.Error) {
                hideProgressBar();
                String message = ((Resource.Error<?>) photoResponseResource).getMessage();
                if (message != null) {
                    Log.e(TAG, "An error occurred " + message);
                    Toast.makeText(getActivity(), "An error occurred " + message, Toast.LENGTH_LONG).show();
                }
            } else if (photoResponseResource instanceof Resource.Loading) {
                showProgressBar();
            }
            if (photoResponseResource != null && photoResponseResource.getData() != null) {
                adapter.updateData(photoResponseResource.getData());
            }
        });
    }

    private void hideProgressBar() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Code to be executed after the delay
                requireActivity().findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                fadeOut(requireActivity().findViewById(R.id.progressBar));
            }
        }, 2000L);
    }

    private void showProgressBar() {
        fadeIn(requireActivity().findViewById(R.id.progressBar));
        requireActivity().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    // Function to apply fade-in animation to a view
    private void fadeIn(View view) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000); // Adjust duration as needed
        view.startAnimation(fadeIn);
        view.setVisibility(View.VISIBLE);
    }

    // Function to apply fade-out animation to a view
    private void fadeOut(View view) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(1000); // Adjust duration as needed
        view.startAnimation(fadeOut);
        view.setVisibility(View.INVISIBLE); // or View.GONE to make it disappear completely
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}