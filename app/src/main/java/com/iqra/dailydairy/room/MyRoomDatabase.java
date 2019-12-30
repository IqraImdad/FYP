package com.iqra.dailydairy.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


import com.iqra.dailydairy.Chain;
import com.iqra.dailydairy.Event;

@Database(entities = {Event.class, Chain.class}, version = 1)
@TypeConverters({Converters.class})

public abstract class MyRoomDatabase extends RoomDatabase {

    public abstract EventDao eventDao();
    public abstract ChainDao chainDao();

    private static volatile MyRoomDatabase INSTANCE;

   public static MyRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyRoomDatabase.class, "event_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}