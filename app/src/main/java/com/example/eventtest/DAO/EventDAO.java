package com.example.eventtest.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.eventtest.entity.EventEntity;

import java.util.List;

@Dao
public interface EventDAO {

    @Query("SELECT * FROM event")
    LiveData<List<EventEntity>> getAllEvent();

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(EventEntity eventEntity);

    @Query("DELETE FROM event")
    void deleteAll();

}
