package com.example.domain.repository;

import com.example.domain.model.ApiResponse;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface SampleRepository {

    Single<Integer> sum(int a, int b);

    Single<ApiResponse> fetchPhotoData(String tag,long pageNo);

    Single<Boolean> fetchPhoto();




}
