package com.dhoomilbsheta.paginggithubusers.viewmodel;

import com.dhoomilbsheta.paginggithubusers.data.NetworkState;
import com.dhoomilbsheta.paginggithubusers.data.datasource.ItemKeyedUserDataSource;
import com.dhoomilbsheta.paginggithubusers.data.datasource.UserDataSourceFactory;
import com.dhoomilbsheta.paginggithubusers.vo.User;

import java.util.concurrent.Executor;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class UserViewModel extends ViewModel {
    public LiveData<PagedList<User>> pagedListLiveData;
    public LiveData<NetworkState> networkState;
    Executor executor;
    LiveData<ItemKeyedUserDataSource> userDataSourceLiveData;

    public UserViewModel() {
        UserDataSourceFactory dataSourceFactory = new UserDataSourceFactory(executor);
        userDataSourceLiveData = dataSourceFactory.getUserLiveData();
        networkState = Transformations.switchMap(dataSourceFactory.getUserLiveData(),
                (Function<ItemKeyedUserDataSource, LiveData<NetworkState>>) ItemKeyedUserDataSource::getNetworkState);
        PagedList.Config config = new PagedList.Config.Builder().setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(20)
                .build();
        pagedListLiveData = (new LivePagedListBuilder<>(dataSourceFactory, config)).build();
    }
}
