package com.example.data.repository;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.data.data.SampleRoomDatabase;
import com.example.data.data.dao.SampleDao;
import com.example.data.models.ApiResponseNw;
import com.example.data.models.ImageDbModel;
import com.example.data.models.PhotoNw;
import com.example.data.network.ApiInterface;
import com.example.data.sharedpreference.SharedPreferenceHelper;
import com.example.domain.model.ApiResponse;
import com.example.domain.model.ImageData;
import com.example.domain.model.Photo;
import com.example.domain.model.Photos;
import com.example.domain.repository.SampleRepository;

//import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Function;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SampleRepositoryImpl implements SampleRepository {

    private ApiInterface apiInterface;
    private SharedPreferenceHelper sharedPreferenceHelper;
    private Context context;

    private SampleRoomDatabase db;

    public SampleRepositoryImpl(ApiInterface apiInterface, SharedPreferenceHelper sharedPreferenceHelper, SampleRoomDatabase db, Context context) {
        this.apiInterface = apiInterface;
        this.sharedPreferenceHelper = sharedPreferenceHelper;
        this.context = context;
        this.db = db;
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

                            Log.e("raman", "fetched photos" + apiResponse.toString());
                            return apiResponse;
                        } else {
                            Log.e("raman", "fetch photos error" + apiResponseNw.getStat());

                            return new ApiResponse(null, "error");
                        }
                    }
                });
    }

    @Override
    public Single<ImageData> fetchPhoto(Photo photo, String tag) {
        return Single.create(new SingleOnSubscribe<ImageData>() {
            @Override
            public void subscribe(SingleEmitter<ImageData> emitter) throws Exception {

                long farm = photo.getFarm();
                String server = photo.getServer();
                String imageId = photo.getId();
                String secret = photo.getSecret();

                String url = "https://farm" + farm + ".staticflickr.com/" + server + "/" + imageId + "_" + secret + ".jpg";

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("request failed: " + e.getMessage());
                        Log.e("raman", e.getMessage());
                        emitter.tryOnError(e);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        InputStream finput = response.body().byteStream();

                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                        int nRead;
                        byte[] data = new byte[16384];
                        while ((nRead = finput.read(data, 0, data.length)) != -1) {
                            buffer.write(data, 0, nRead);
                        }
                        byte[] bytes = buffer.toByteArray();
                        String encoded = Base64.getEncoder().encodeToString(bytes);

                        ImageDbModel imageDbModel = new ImageDbModel(tag, encoded, photo.getTitle(), photo.getId());
                        db.sampleDao().insertImage(imageDbModel);

                        ImageData imageData = new ImageData(encoded, photo.getTitle(), photo.getId(), tag);
                        emitter.onSuccess(imageData);
                    }

                });


            }
        });
    }

    @Override
    public Single<List<ImageData>> getFromDb(String tag) {
        return db.sampleDao().getAllImages(tag).map(new Function<List<ImageDbModel>, List<ImageData>>() {
            @Override
            public List<ImageData> apply(List<ImageDbModel> imageDbModels) throws Exception {

                List<ImageData> imageDataList = new ArrayList<>();
                for (ImageDbModel imageDbModel : imageDbModels) {
                    ImageData imageData = new ImageData(imageDbModel.getImageBase64(), imageDbModel.getTitle(), imageDbModel.getImageId(), tag);
                    imageDataList.add(imageData);
                }

                return imageDataList;
            }
        });
    }

}
