package com.example.data.network;

import com.example.data.models.ApiResponseNw;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("rest/")
    Single<ApiResponseNw> fetchPhotosList(
            @Query("method") String method,
            @Query("api_key") String apiKey,
            @Query("tags") String tags,
            @Query("per_page") long perPage,
            @Query("page") long page,
            @Query("format") String format,
            @Query("nojsoncallback") int noJson
    );


}
