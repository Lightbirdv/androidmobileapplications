package com.example.learningcardroomapp;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LearningCardRepository {

    private LearningCardDao LearningCardDao;
    private LiveData<List<LearningCard>> AllLearningCards;
    public LearningCard SingleLearningCard;

    LearningCardRepository(Application application) {
        LearningCardDatabase db =  LearningCardDatabase.getDatabase(application);
        LearningCardDao = db.learningCardDao();
        AllLearningCards = LearningCardDao.getAll();
    }

    LiveData<List<LearningCard>> getAll() {
        return AllLearningCards;
    }

    List<LearningCard> getAllLC() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<LearningCard>> future = executor.submit(new Callable<List<LearningCard>>() {
            @Override
            public List<LearningCard> call() {
                List<LearningCard> l;
                l = LearningCardDao.getAllLC();
                return l;
            }
        });
        List<LearningCard> l = null;
        try {
            l = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return l;
    }

    LearningCard getSingle(long learningcard_id) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<LearningCard> future = executor.submit(new Callable<LearningCard>() {
            @Override
            public LearningCard call() {
                LearningCard l;
                l = LearningCardDao.getSingle(learningcard_id);
                return l;
            }
        });
        LearningCard l = null;
        try {
            l = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return l;
    }

    List<LearningCard> getLCfromSubject(String subject) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<LearningCard>> future = executor.submit(new Callable<List<LearningCard>>() {
            @Override
            public List<LearningCard> call() {
                List<LearningCard> l;
                l = LearningCardDao.getLCfromSubject(subject);
                return l;
            }
        });
        List<LearningCard> l = null;
        try {
            l = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return l;
    }

    void insert(LearningCard learningCard) {
        LearningCardDatabase.databaseWriteExecutor.execute(() -> {
            LearningCardDao.insert(learningCard);
        });
    }

    void insertAll(LearningCard...learningCards){
        LearningCardDatabase.databaseWriteExecutor.execute(() -> {
            LearningCardDao.insertAll(learningCards);
        });
    }

    void update(LearningCard learningCard){
        LearningCardDatabase.databaseWriteExecutor.execute(() -> {
            LearningCardDao.update(learningCard);
        });
    }

    void delete(LearningCard learningCard) {
        LearningCardDatabase.databaseWriteExecutor.execute(() -> {
            LearningCardDao.delete(learningCard);
        });
    }

    void deleteAll() {
        LearningCardDatabase.databaseWriteExecutor.execute(() -> {
            LearningCardDao.deleteAll();
        });
    }
}
