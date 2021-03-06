package com.example.domain.repository;

import com.example.domain.model.ApiResponse;
import com.example.domain.model.ImageData;
import com.example.domain.model.Photo;

import java.util.List;

import io.reactivex.Single;

public interface SampleRepository {

    Single<Integer> sum(int a, int b);

    Single<ApiResponse> fetchPhotoData(String tag,long pageNo);

    Single<ImageData> fetchPhoto(Photo photo, String tag, int pageNumber);

    Single<List<ImageData>> getFromDb(String tag);



}
