package com.wayfair.labs.retrofitdemo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitHubService {

    @GET("search/repositories")
    Observable<SearchResponse> searchRepos(@Query("q") String query);
}
