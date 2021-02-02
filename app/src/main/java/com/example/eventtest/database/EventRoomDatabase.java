package com.example.eventtest.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.eventtest.DAO.EventDAO;
import com.example.eventtest.entity.EventEntity;

@Database(entities = {EventEntity.class},version = 1,exportSchema = false)
public abstract class EventRoomDatabase extends RoomDatabase {

    public abstract EventDAO eventDAO();
    private static EventRoomDatabase INSTANCE;

    public static EventRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (EventRoomDatabase.class){
                if(INSTANCE == null){
                     INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                             EventRoomDatabase.class,"event_database")
                             // Wipes and rebuilds instead of migrating
                             // if no Migration object.
                             // Migration is not part of this practical.
                             .fallbackToDestructiveMigration()
                             .addCallback(sRoomDatabaseCallback)
                             .build();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     */

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final EventDAO mDao;
        String [] events = {"Holi", "Diwali", "RakshaBandhan", "Republic Day", "Independence Day"," 2nd October"};

        PopulateDbAsync(EventRoomDatabase db) {
            mDao = db.eventDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            //mDao.deleteAll();

            for( int i = 0; i <= events.length - 1; i++) {
                EventEntity event = new EventEntity(events[i]);
                mDao.insert(event);
            }

            return null;

        }
    }
}
