package com.dhoomilbsheta.paginggithubusers.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.github.com";
    private static ApiClient instance = null;
    private Retrofit retrofit;
    private GithubService githubService;

    private ApiClient() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        githubService = retrofit.create(GithubService.class);
    }

    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public GithubService githubService() {
        return githubService;
    }
}
