package com.iqra.dailydairy.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iqra.dailydairy.Event;

import java.util.List;

@Dao
public interface EventDao {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Event event);

    @Query("DELETE FROM event")
    void deleteAll();

    @Query("SELECT * from event ORDER BY day ASC")
    List<Event> getEvents();

    @Query("select * from event where month = :month and year = :year")
    List<Event> getEventsOfMonth(String month, String year);
}
