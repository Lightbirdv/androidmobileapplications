package com.example.learningcardroomapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LearningCardViewModel extends AndroidViewModel {

    private LearningCardRepository repository;
    private final LiveData<List<LearningCard>> AllLearningCards;
    private List<LearningCard> AllLC;
    public LearningCard SingleLearningCard;

    public LearningCardViewModel (Application application) {
        super(application);
        repository = new LearningCardRepository(application);
        AllLearningCards = repository.getAll();
        AllLC = repository.getAllLC();
    }

    LiveData<List<LearningCard>> getAll() {
        return AllLearningCards;
    }

    List<LearningCard> getAllLC(){
        return AllLC;
    }

    LearningCard getSingle(long id) {
        SingleLearningCard = repository.getSingle(id);
        return repository.getSingle(id);
    }

    List<LearningCard> getLCfromSubject(String subject) {
        return repository.getLCfromSubject(subject);
    }

    void update(LearningCard learningCard){
        repository.update(learningCard);
    }

    void delete(LearningCard learningCard) {
        repository.delete(learningCard);
    }

    void deleteAll() {
        repository.deleteAll();
    }

    public void insertAll(LearningCard...learningCards) {
        repository.insertAll(learningCards);
    }

    public void insert(LearningCard learningCard) {
        repository.insert(learningCard);
    }
}
