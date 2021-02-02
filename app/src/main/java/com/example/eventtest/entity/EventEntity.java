package com.example.eventtest.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "event")
public class EventEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "eventName")
    String eventname;

    public String getEventname() {
        return eventname;
    }

    public EventEntity(@NonNull String eventname) {
        this.eventname = eventname;
    }
}
