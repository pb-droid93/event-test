package com.example.eventtest.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.eventtest.entity.EventEntity;
import com.example.eventtest.repo.EventRepo;

import java.util.List;


public class EventViewModel extends AndroidViewModel {

    private EventRepo eventRepo;
    private LiveData<List<EventEntity>> allEvent;

    /**
     * Add a constructor that gets a reference to the taskRepository and
     * gets the list of all task from the TaskRepository.
     * @param application
     */
    public EventViewModel(@NonNull Application application) {
        super(application);
        eventRepo = new EventRepo(application);
        allEvent = eventRepo.getEventList();
    }

    /**
     * Add a "getter" method that gets all the words. This completely hides the implementation from the UI.
     * @return
     */
    public LiveData<List<EventEntity>> getAllTask() {
        return allEvent;
    }

    /**
     * Create a wrapper insert() method that calls the Repository's insert() method.
     * In this way, the implementation of insert() is completely hidden from the UI.
     * @param
     */
    public void insert(EventEntity eventEntity){
        eventRepo.insert(eventEntity);
    }

}
