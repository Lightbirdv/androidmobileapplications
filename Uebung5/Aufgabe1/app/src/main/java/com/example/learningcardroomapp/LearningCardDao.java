package com.example.learningcardroomapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LearningCardDao {

    // get all learningCards as LiveData
    @Query("SELECT * FROM learningcard_table")
    LiveData<List<LearningCard>> getAll();

    // get all learningCards as List
    @Query("SELECT * FROM learningcard_table")
    List<LearningCard> getAllLC();

    @Query("SELECT * FROM learningcard_table WHERE learningCardId=:id")
    LearningCard getSingle(long id);

    @Query("SELECT * FROM learningcard_table WHERE subject LIKE :subject")
    List<LearningCard> getLCfromSubject(String subject);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LearningCard learningCard);

    @Insert
    void insertAll(LearningCard... learningCards);

    @Update
    void update(LearningCard learningCard);

    @Delete
    void delete(LearningCard learningCard);

    @Query("DELETE FROM learningcard_table")
    void deleteAll();
}
