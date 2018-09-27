package com.dhoomilbsheta.paginggithubusers.api;

import com.dhoomilbsheta.paginggithubusers.vo.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubService {
    @GET("/users")
    Call<List<User>> getUser(@Query("since") long since, @Query("perPage") int perPage);
}
