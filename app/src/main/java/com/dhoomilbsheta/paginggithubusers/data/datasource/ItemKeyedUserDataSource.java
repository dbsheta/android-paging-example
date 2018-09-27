package com.dhoomilbsheta.paginggithubusers.data.datasource;

import android.util.Log;

import com.dhoomilbsheta.paginggithubusers.api.ApiClient;
import com.dhoomilbsheta.paginggithubusers.api.GithubService;
import com.dhoomilbsheta.paginggithubusers.data.NetworkState;
import com.dhoomilbsheta.paginggithubusers.vo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.ItemKeyedDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemKeyedUserDataSource extends ItemKeyedDataSource<Long, User> {
    public static final String TAG = "ItemKeyedUserDataSource";
    GithubService githubService;
    LoadInitialParams<Long> longLoadInitialParams;
    LoadParams<Long> longLoadParams;

    private MutableLiveData<NetworkState> networkState;
    private MutableLiveData<NetworkState> initialLoading;

    private Executor executor;

    public ItemKeyedUserDataSource(Executor executor) {
        githubService = ApiClient.getInstance().githubService();
        networkState = new MutableLiveData<>();
        initialLoading = new MutableLiveData<>();
        this.executor = executor;
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitalLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<User> callback) {
        Log.d(TAG, "Loading Range " + 1 + " Count " + params.requestedLoadSize);
        final List<User> users = new ArrayList<>();
        longLoadInitialParams = params;

        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        githubService.getUser(1, params.requestedLoadSize).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    users.addAll(response.body());
                    callback.onResult(users);
                    initialLoading.postValue(NetworkState.LOADED);
                    networkState.postValue(NetworkState.LOADED);
                    longLoadInitialParams = null;
                } else {
                    Log.e("API CALL", response.message());
                    initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                String error;
                error = t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, error));
            }
        });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull final LoadCallback<User> callback) {
        Log.d(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);
        final List<User> users = new ArrayList<>();
        longLoadParams = params;

        networkState.postValue(NetworkState.LOADING);
        githubService.getUser(params.key, params.requestedLoadSize).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    users.addAll(response.body());
                    callback.onResult(users);
                    networkState.postValue(NetworkState.LOADED);
                    longLoadParams = null;
                } else {
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                    Log.e("API CALL", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                String error;
                error = t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, error));
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<User> callback) {

    }

    @NonNull
    @Override
    public Long getKey(@NonNull User item) {
        return item.getId();
    }
}
