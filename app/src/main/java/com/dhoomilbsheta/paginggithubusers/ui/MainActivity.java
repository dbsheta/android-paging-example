package com.dhoomilbsheta.paginggithubusers.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.dhoomilbsheta.paginggithubusers.R;
import com.dhoomilbsheta.paginggithubusers.ui.adapter.UserPagedAdapter;
import com.dhoomilbsheta.paginggithubusers.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {
    private UserViewModel viewModel;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.userList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        final UserPagedAdapter userAdapter = new UserPagedAdapter();

        viewModel.pagedListLiveData.observe(this, userAdapter::submitList);
        viewModel.networkState.observe(this, networkState -> {
            userAdapter.setNetworkState(networkState);
            Log.d(TAG, "Network State Change");
        });

        recyclerView.setAdapter(userAdapter);

    }
}
