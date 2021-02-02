package com.example.eventtest.repo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.eventtest.DAO.EventDAO;
import com.example.eventtest.database.EventRoomDatabase;
import com.example.eventtest.entity.EventEntity;

import java.util.List;

public class EventRepo {
    private EventDAO eventDAO;
    private LiveData<List<EventEntity>> eventList;

    public EventRepo(Application application){
        EventRoomDatabase eventRoomDatabase = EventRoomDatabase.getDatabase(application);
        eventDAO = eventRoomDatabase.eventDAO();
        eventList = eventDAO.getAllEvent();
    }

    public LiveData<List<EventEntity>> getEventList(){
        return eventList;
    };

    /**
     * Add a wrapper for the insert() method.
     */
    public void insert(EventEntity eventEntity){
        new insertAsyncTask(eventDAO).execute(eventEntity);
    }

    /**
     * insertAsyncTask as an inner class.
     * You should be familiar with AsyncTask, so here is the insertAsyncTask code for you to copy:
     */

    private static class insertAsyncTask extends AsyncTask<EventEntity,Void,Void> {

        private  EventDAO asyncTaskDao;
        insertAsyncTask(EventDAO mtaskDao){

            asyncTaskDao = mtaskDao;
        }

        @Override
        protected Void doInBackground(final EventEntity... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
