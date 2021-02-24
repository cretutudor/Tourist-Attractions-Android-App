package com.example.proiect;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Attraction.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "TouristAttractions";
    private static Database sInstance;

    public static Database getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context, Database.class, Database.DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract AttractionDAO attractionDAO();
}
