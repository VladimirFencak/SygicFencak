package com.example.sygicfencak.data.location_data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sygicfencak.domain.model.Location

@Database(
    entities = [Location::class],
    version = 1
)
abstract class LocationDatabase: RoomDatabase(){
    abstract val noteDao: LocationDao

}