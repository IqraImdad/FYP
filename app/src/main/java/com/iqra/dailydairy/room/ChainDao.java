package com.iqra.dailydairy.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iqra.dailydairy.Chain;
import com.iqra.dailydairy.Event;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ChainDao {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Chain chain);

    @Query("DELETE FROM Chain")
    void deleteAll();

    @Delete
    void deleteChain(Chain chain);

    @Query("update chain set events =:events where id = :id")
    void update(ArrayList<Event> events, String id);

    @Query("SELECT * from Chain")
    List<Chain> getChains();

    @Query("SELECT * from Chain where id = :id")
    Chain getChains(String id);

}
