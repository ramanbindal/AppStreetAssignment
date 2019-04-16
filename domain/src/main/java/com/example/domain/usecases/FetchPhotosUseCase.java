package com.example.domain.usecases;

import com.example.domain.base.SingleUseCase;
import com.example.domain.executor.PostExecutionThread;
import com.example.domain.model.ApiResponse;
import com.example.domain.model.ImageData;
import com.example.domain.model.Photo;
import com.example.domain.repository.SampleRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import sun.rmi.runtime.Log;

public class FetchPhotosUseCase extends SingleUseCase<List<ImageData>, FetchPhotosUseCase.Params> {
    private SampleRepository sampleRepository;

    public FetchPhotosUseCase(PostExecutionThread postExecutionThread, SampleRepository sampleRepository) {
        super(postExecutionThread);
        this.sampleRepository = sampleRepository;
    }

    @Override
    public Single<List<ImageData>> buildUseCaseObservable(final FetchPhotosUseCase.Params params) {
        return sampleRepository.fetchPhotoData(params.tag, params.pageNumber)
                .flatMap(new Function<ApiResponse, SingleSource<List<ImageData>>>() {
                    @Override
                    public SingleSource<List<ImageData>> apply(ApiResponse apiResponse) throws Exception {
                        List<Single<ImageData>> singles = new ArrayList<>();

                        if (apiResponse.getStat().equalsIgnoreCase("ok")) {
                            if (apiResponse.getPhotos() != null && apiResponse.getPhotos().getPhoto() != null&&!apiResponse.getPhotos().getPhoto().isEmpty()) {

                                for (int i = 0; i < apiResponse.getPhotos().getPhoto().size(); i++) {
                                    ImageData imageData = new ImageData();
                                    singles.add(sampleRepository.fetchPhoto(apiResponse.getPhotos().getPhoto().get(i), params.tag,params.pageNumber)
                                            .onErrorReturnItem(imageData));
                                }


                                return Single.zip(singles, new Function<Object[], List<ImageData>>() {
                                    @Override
                                    public List<ImageData> apply(Object[] objects) throws Exception {
                                        List<ImageData> imageDataList = new ArrayList<>();
                                        for (Object i : objects) {
                                            ImageData imageData = (ImageData) i;
                                            imageDataList.add(imageData);
                                        }
                                        return imageDataList;
                                    }
                                });

                            }
                        }
                        return null;
                    }
                });


    }

    public static final class Params {

        private int pageNumber;
        private String tag;

        public Params(int pageNumber, String tag) {
            this.pageNumber = pageNumber;
            this.tag = tag;
        }

        public static FetchPhotosUseCase.Params fetchPhotosUseCase(int pageNumber, String tag) {
            return new FetchPhotosUseCase.Params(pageNumber, tag);
        }
    }
}
