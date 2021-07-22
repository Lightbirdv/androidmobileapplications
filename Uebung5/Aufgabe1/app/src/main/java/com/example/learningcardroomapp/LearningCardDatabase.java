package com.example.learningcardroomapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {LearningCard.class}, version = 1, exportSchema = false)
public abstract class LearningCardDatabase extends RoomDatabase {

    public abstract LearningCardDao learningCardDao();

    private static volatile LearningCardDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static LearningCardDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LearningCardDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LearningCardDatabase.class, "learning_card_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                LearningCardDao lDao = INSTANCE.learningCardDao();
                lDao.deleteAll();

                lDao.insert(new LearningCard("How can I create a new LearningCard?", "By pressing on the + Fab button", "Mobile Anwendungsentwicklung"));
                lDao.insert(new LearningCard("Do I need to prepare for the Web Engineering II exam?", "Maybe? I dunno", "Web Engineering II"));
                lDao.insert(new LearningCard("Can I do it this time?", "hopefully", "Programmierung II"));
            });
        }
    };
}
