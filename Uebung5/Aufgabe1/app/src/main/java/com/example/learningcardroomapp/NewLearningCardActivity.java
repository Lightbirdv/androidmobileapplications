package com.example.learningcardroomapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


public class NewLearningCardActivity extends AppCompatActivity {

    private EditText e_question,e_answer,e_subject;
    private Button b_save;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_learning_card);
        e_question = findViewById(R.id.edit_question);
        e_answer = findViewById(R.id.edit_answer);
        e_subject = findViewById(R.id.edit_subject);
        b_save = findViewById(R.id.button_save);

        // When save button is clicked check if EditTexts are empty if not create new LearningCard and return Result_ok
        // otherwise return Result_canceled in replyIntent
        b_save.setOnClickListener(view -> {
            Intent replyIntent = new Intent(NewLearningCardActivity.this, MainActivity.class);
            if (TextUtils.isEmpty(e_question.getText()) || TextUtils.isEmpty(e_answer.getText()) || TextUtils.isEmpty(e_subject.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String question = e_question.getText().toString();
                String answer = e_answer.getText().toString();
                String subject = e_subject.getText().toString();
                LearningCard resL = new LearningCard(question,answer,subject);
                LearningCardViewModel mLearningCardViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LearningCardViewModel.class);
                mLearningCardViewModel.insert(resL);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}
