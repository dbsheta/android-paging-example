package com.dhoomilbsheta.paginggithubusers.data.datasource;

import java.util.concurrent.Executor;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class UserDataSourceFactory extends DataSource.Factory {
    MutableLiveData<ItemKeyedUserDataSource> userLiveData;
    ItemKeyedUserDataSource userDataSource;
    Executor executor;

    public UserDataSourceFactory(Executor executor) {
        this.userLiveData = new MutableLiveData<>();
        this.executor = executor;
    }

    @Override
    public DataSource create() {
        userDataSource = new ItemKeyedUserDataSource(executor);
        userLiveData.postValue(userDataSource);
        return userDataSource;
    }

    public MutableLiveData<ItemKeyedUserDataSource> getUserLiveData() {
        return userLiveData;
    }
}
