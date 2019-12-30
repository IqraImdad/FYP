package com.iqra.dailydairy.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iqra.dailydairy.Chain;
import com.iqra.dailydairy.Event;

import java.util.List;

@Dao
public interface ChainDao {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Chain event);

    @Query("DELETE FROM Chain")
    void deleteAll();

    @Query("SELECT * from Chain")
    List<Chain> getChains();

}
