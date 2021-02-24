package com.example.proiect;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proiect.Attraction;

import java.util.List;

@Dao
public interface AttractionDAO {
    @Query("select * from Attractions order by id")
    List<Attraction> loadAttractions();

    @Insert
    long insertAttraction(Attraction attraction);

    @Update
    void updateAttraction(Attraction attraction);

    @Delete
    void deleteAttraction(Attraction attraction);
}
