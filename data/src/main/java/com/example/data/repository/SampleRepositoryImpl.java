package com.example.data.repository;

import android.content.Context;

import com.example.data.data.SampleRoomDatabase;
import com.example.data.data.dao.SampleDao;
import com.example.data.models.ApiResponseNw;
import com.example.data.models.PhotoNw;
import com.example.data.network.ApiInterface;
import com.example.data.sharedpreference.SharedPreferenceHelper;
import com.example.domain.model.ApiResponse;
import com.example.domain.model.Photo;
import com.example.domain.model.Photos;
import com.example.domain.repository.SampleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.functions.Function;

public class SampleRepositoryImpl implements SampleRepository {

    private ApiInterface apiInterface;
    private SharedPreferenceHelper sharedPreferenceHelper;
    private Context context;

    private SampleDao sampleDao;
    private SampleRoomDatabase db;

    public SampleRepositoryImpl(ApiInterface apiInterface, SharedPreferenceHelper sharedPreferenceHelper, SampleRoomDatabase db, Context context) {
        this.apiInterface = apiInterface;
        this.sharedPreferenceHelper = sharedPreferenceHelper;
        this.context = context;
        this.db = db;
        sampleDao = db.wordDao();
    }


    @Override
    public Single<Integer> sum(final int a, final int b) {
        return Single.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return (a + b);
            }
        });
    }

    @Override
    public Single<ApiResponse> fetchPhotoData(String tag, long pageNo) {
        return apiInterface.fetchPhotosList("flickr.photos.search", "a2413c976c29140174b2467b26e8db5c", tag, 15, pageNo, "json", 1)
                .map(new Function<ApiResponseNw, ApiResponse>() {
                    @Override
                    public ApiResponse apply(ApiResponseNw apiResponseNw) throws Exception {
                        if (apiResponseNw.getStat().equalsIgnoreCase("ok")) {

                            List<Photo> photoList = new ArrayList<>();
                            for (int i = 0; i < apiResponseNw.getPhotos().getPhoto().size(); i++) {
                                PhotoNw photoNw = apiResponseNw.getPhotos().getPhoto().get(i);
                                Photo photo = new Photo(photoNw.getId(), photoNw.getOwner(), photoNw.getSecret(), photoNw.getServer(), photoNw.getFarm(), photoNw.getTitle(), photoNw.getIspublic(), photoNw.getIsfriend(), photoNw.getIsfamily());
                                photoList.add(photo);
                            }
                            Photos photos = new Photos(apiResponseNw.getPhotos().getPage(),
                                    apiResponseNw.getPhotos().getPages(),
                                    apiResponseNw.getPhotos().getPerpage(), apiResponseNw.getPhotos().getTotal(), photoList);
                            ApiResponse apiResponse = new ApiResponse(photos, apiResponseNw.getStat());
                            return apiResponse;
                        }
                        else
                        {
                            return new ApiResponse(null,"error");
                        }
                    }
                });
    }

}
