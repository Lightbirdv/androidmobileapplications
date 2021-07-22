package com.example.learningcardroomapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "learningcard_table")
public class LearningCard {

    @PrimaryKey(autoGenerate = true)
    private Integer learningCardId;

    @NonNull
    private String subject;

    @NonNull
    private String question;

    @NonNull
    private String answer;

    @NonNull
    private Boolean learned;

    public LearningCard(@NonNull String question, @NonNull String answer, @NonNull String subject) {
        this.question = question;
        this.answer = answer;
        this.subject = subject;
        this.learned = false;
    }

    // getter-methods
    public Integer getLearningCardId() {
        return learningCardId;
    }

    @NonNull
    public String getSubject() {
        return subject;
    }

    @NonNull
    public String getQuestion() {
        return question;
    }

    @NonNull
    public String getAnswer() {
        return answer;
    }

    @NonNull
    public Boolean getLearned() {
        return learned;
    }

    //setter-methods
    public void setLearningCardId(Integer learningCardId) {
        this.learningCardId = learningCardId;
    }

    public void setSubject(@NonNull String subject) {
        this.subject = subject;
    }

    public void setQuestion(@NonNull String question) {
        this.question = question;
    }

    public void setAnswer(@NonNull String answer) {
        this.answer = answer;
    }

    public void setLearned(@NonNull Boolean learned) {
        this.learned = learned;
    }
}
