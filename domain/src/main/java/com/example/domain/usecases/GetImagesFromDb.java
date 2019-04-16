package com.example.domain.usecases;

import com.example.domain.base.SingleUseCase;
import com.example.domain.executor.PostExecutionThread;
import com.example.domain.model.ImageData;
import com.example.domain.repository.SampleRepository;

import java.util.List;

import io.reactivex.Single;

public class GetImagesFromDb extends SingleUseCase<List<ImageData>, GetImagesFromDb.Params> {
    private SampleRepository sampleRepository;

    public GetImagesFromDb(PostExecutionThread postExecutionThread, SampleRepository sampleRepository) {
        super(postExecutionThread);
        this.sampleRepository = sampleRepository;
    }

    @Override
    public Single<List<ImageData>> buildUseCaseObservable(Params params) {
        return sampleRepository.getFromDb(params.tag);
    }

    public static final class Params {

        private String tag;

        public Params(String tag) {
            this.tag = tag;
        }

        public static GetImagesFromDb.Params getImagesFromDb(String tag) {
            return new GetImagesFromDb.Params(tag);
        }
    }
}
