package com.example.learningcardroomapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class DetailedLearningCardActivity extends AppCompatActivity {
    public static final String EXTRA_LEARNING_CARD_ID = "com.example.wanderrouteroom.EXTRA_LEARNING_CARD_ID";
    public static final String EXTRA_LEARNING_CARD_QUESTION = "com.example.wanderrouteroom.EXTRA_LEARNING_CARD_QUESTION";
    public static final String EXTRA_LEARNING_CARD_ANSWER = "com.example.wanderrouteroom.EXTRA_LEARNING_CARD_ANSWER";
    public static final String EXTRA_LEARNING_CARD_SUBJECT = "com.example.wanderrouteroom.EXTRA_LEARNING_CARD_SUBJECT";
    public static final String EXTRA_LEARNING_CARD_LEARNED = "com.example.wanderrouteroom.EXTRA_LEARNING_CARD_LEARNED";
    public static final int EDIT_LEARNING_CARD_ACTIVITY_REQUEST_CODE = 1;
    private TextView t_question,t_answer,t_subject;
    private Button b_mark_as_learned, b_reveal_answer, b_edit;
    private int id;
    private String question, answer, subject, learned;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_learning_card);
        t_question = findViewById(R.id.text_question);
        t_subject = findViewById(R.id.text_subject);
        t_answer = findViewById(R.id.text_answer);
        b_edit = findViewById(R.id.button_edit);
        b_reveal_answer = findViewById(R.id.button_reveal);
        b_mark_as_learned = findViewById(R.id.button_mark_as_learned);

        // Get data from Intent and put them into the variables
        Intent data = getIntent();
        id = data.getIntExtra(DetailedLearningCardActivity.EXTRA_LEARNING_CARD_ID, -1);
        question = data.getStringExtra(DetailedLearningCardActivity.EXTRA_LEARNING_CARD_QUESTION);
        answer = data.getStringExtra(DetailedLearningCardActivity.EXTRA_LEARNING_CARD_ANSWER);
        subject = data.getStringExtra(DetailedLearningCardActivity.EXTRA_LEARNING_CARD_SUBJECT);
        learned = data.getStringExtra(DetailedLearningCardActivity.EXTRA_LEARNING_CARD_LEARNED);

        // Set TextViews with values
        t_question.setText(question);
        t_subject.setText("Subject: " + subject);

        LearningCardViewModel mLearningCardViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LearningCardViewModel.class);

        // override on click listener and put if statements to differentiate between buttons
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if reveal button is clicked set the Text for the Answer
                if (v.getId() == R.id.button_reveal) {
                    t_answer.setText(answer);
                }
                // if edit button is clicked open NewLearningCardActivity but fill the EditText with values from the learningCard
                if (v.getId() == R.id.button_edit) {
                    Intent intent = new Intent(DetailedLearningCardActivity.this,EditLearningCardActivity.class);
                    intent.putExtra(EditLearningCardActivity.EXTRA_LEARNING_CARD_ID_EDIT, id);
                    startActivityForResult(intent, EDIT_LEARNING_CARD_ACTIVITY_REQUEST_CODE);
                }
                // if mark as learned button is clicked it will update the learningCard as learned
                if (v.getId() == R.id.button_mark_as_learned) {
                    LearningCard learningCard = mLearningCardViewModel.getSingle(id);
                    if(learningCard.getLearned() != true) {
                        learningCard.setLearned(true);
                        mLearningCardViewModel.update(learningCard);
                    }
                }

            }
        };
        b_mark_as_learned.setOnClickListener(listener);
        b_reveal_answer.setOnClickListener(listener);
        b_edit.setOnClickListener(listener);
    }

}
