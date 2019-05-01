package com.example.presentation.ui.firstFragment;

import com.example.domain.model.ImageData;
import com.example.domain.usecases.FetchPhotosUseCase;
import com.example.domain.usecases.GetImagesFromDb;
import com.example.presentation.base.BaseViewModel;

import java.util.List;

import io.reactivex.observers.DisposableSingleObserver;

public class GridFragmentViewModel extends BaseViewModel<GridFragmentNavigator> {

    private FetchPhotosUseCase fetchPhotosUseCase;
    private GetImagesFromDb getImagesFromDb;

    public GridFragmentViewModel(FetchPhotosUseCase fetchPhotosUseCase, GetImagesFromDb getImagesFromDb) {
        this.fetchPhotosUseCase = fetchPhotosUseCase;
        this.getImagesFromDb = getImagesFromDb;
    }

    public void fetchPhotos(String tag, int pageNo) {
        fetchPhotosUseCase.execute(new DisposableSingleObserver<List<ImageData>>() {
            @Override
            public void onSuccess(List<ImageData> imageData) {
                if (imageData != null)
                    getNavigator().onListLoaded(imageData);
                else
                    getImagesFromDb(tag);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getImagesFromDb(tag);

            }
        }, FetchPhotosUseCase.Params.fetchPhotosUseCase(pageNo, tag));
    }

    private void getImagesFromDb(String tag) {
        getImagesFromDb.execute(new DisposableSingleObserver<List<ImageData>>() {
            @Override
            public void onSuccess(List<ImageData> imageData) {
                if (imageData != null && !imageData.isEmpty()) {
                    getNavigator().onListLoaded(imageData);
                    getNavigator().dataLoadedFromDb();
                }
                else
                {
                    getNavigator().noData();
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        }, GetImagesFromDb.Params.getImagesFromDb(tag));
    }
}
