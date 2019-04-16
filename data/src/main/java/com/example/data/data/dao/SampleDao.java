package com.example.data.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.data.models.ImageDbModel;

import java.util.List;

import io.reactivex.Single;

//this is the DAO(Data Access Object) which generates the SQL queries using annotations such as @Insert
//Room uses the DAO to create a clean API for your code.
@Dao
public interface SampleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImage(ImageDbModel imageDbModel);

    @Query("select * from image_table where image_tag =:tag ")
    Single<List<ImageDbModel>> getAllImages(String tag);

//    @Query("delete from ImageDbModel")
//    void deleteAll();

//    @Query("select * from ImageDbModel order by word_id asc")
//    Flowable<List<ImageDbModel>> getAllWords();
//    //LiveData<T> has no public methods to set/post value in LiveData as here we won't be adding values to this list
    //it will be synced with the room database
    //If the developer wants to update the contents of LiveData then he/she should use MutableLiveData which has
    //setValue(T) and postValue(T) methods
    //Usually, MutableLiveData is used in the ViewModel, and then the ViewModel only exposes immutable LiveData objects to the observers.

}


/*

You create an Observer of the data in the onCreate() method of MainActivity and override the observer's onChanged()
method. When the LiveData changes, the observer is notified and onChanged() is executed. You will then update the
cached data in the adapter, and the adapter will update what the user sees.
 */