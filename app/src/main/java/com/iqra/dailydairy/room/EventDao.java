package com.iqra.dailydairy.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.iqra.dailydairy.Chain;
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

    @Query("SELECT * from event")
    List<Event> getAllEvents();

    @Update
    void updateEvent(Event event);

    @Query("select * from event where month = :month and year = :year")
    List<Event> getEventsOfMonth(String month, String year);

    @Query("select * from event where day = :day")
    List<Event> getEventsOfDay(String day);
}
