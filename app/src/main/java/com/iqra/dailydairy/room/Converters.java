package com.iqra.dailydairy.room;

import androidx.room.TypeConverter;


import com.iqra.dailydairy.Event;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.util.ArrayList;

class Converters {

    @TypeConverter
    public static ArrayList<Event> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Event>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Event> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
