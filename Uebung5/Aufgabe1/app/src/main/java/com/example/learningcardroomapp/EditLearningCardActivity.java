package com.example.learningcardroomapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class EditLearningCardActivity extends AppCompatActivity {

    public static final String EXTRA_LEARNING_CARD_ID_EDIT = "com.example.wanderrouteroom.EXTRA_LEARNING_CARD_ID_EDIT";

    private EditText e_question,e_answer,e_subject;
    private Button b_save;
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_learning_card);
        e_question = findViewById(R.id.edit_question);
        e_answer = findViewById(R.id.edit_answer);
        e_subject = findViewById(R.id.edit_subject);
        b_save = findViewById(R.id.button_save);

        // get the id from Intent and search for the learningCard, then fill EditText with values
        Intent data = getIntent();
        id = data.getIntExtra(EditLearningCardActivity.EXTRA_LEARNING_CARD_ID_EDIT, -1);
        LearningCardViewModel mLearningCardViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LearningCardViewModel.class);

        LearningCard learningCard = mLearningCardViewModel.getSingle(id);
        e_question.setText(learningCard.getQuestion());
        e_answer.setText(learningCard.getAnswer());
        e_subject.setText(learningCard.getSubject());

        b_save.setOnClickListener(view -> {
            Intent replyIntent = new Intent(EditLearningCardActivity.this, MainActivity.class);
            if (TextUtils.isEmpty(e_question.getText()) || TextUtils.isEmpty(e_answer.getText()) || TextUtils.isEmpty(e_subject.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String question = e_question.getText().toString();
                String answer = e_answer.getText().toString();
                String subject = e_subject.getText().toString();
                // set values of learningCard object and update
                learningCard.setQuestion(question);
                learningCard.setAnswer(answer);
                learningCard.setSubject(subject);
                mLearningCardViewModel.update(learningCard);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}
