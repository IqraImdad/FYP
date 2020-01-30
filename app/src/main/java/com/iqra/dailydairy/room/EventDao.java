package com.iqra.dailydairy.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELECT * from event where isDeleted = 0 ORDER BY day ASC")
    List<Event> getEvents();

    @Query("SELECT * from event where id = :id and isDeleted = 0")
    Event getEvent(String id);

    @Query("SELECT * from event where isDeleted = 0")
    List<Event> getAllEvents();

    @Update
    void updateEvent(Event event);

    @Query("update event set isDeleted = 1 where id = :id")
    void deleteEvent(String id);

    @Query("select * from event where month = :month and year = :year and isDeleted = 0")
    List<Event> getEventsOfMonth(String month, String year);

    @Query("select * from event where day = :day and isDeleted = 0")
    List<Event> getEventsOfDay(String day);
}
