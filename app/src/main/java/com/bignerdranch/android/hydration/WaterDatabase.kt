package com.bignerdranch.android.hydration

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WaterRecord::class], version = 1)
abstract class WaterDatabase : RoomDatabase() {

    abstract fun waterDao(): WaterDao

    companion object {

        // singleton pattern : only one copy
        @Volatile // compiler is smart and sometimes can create an instance ahead of time and this annotation tells it not to.
        private var INSTANCE: WaterDatabase? = null

        fun getDatabase(context: Context): WaterDatabase {
            /* This function checks if there is a WaterDatabase instance, if there is it will return it otherwise it
            * will create one */
            return INSTANCE
                ?: synchronized(this) { // accessed by single thread at a time - synchronized
                    val instance = Room.databaseBuilder(
                        context,
                        WaterDatabase::class.java,
                        "water_database",
                    )
                        .build()
                    INSTANCE = instance
                    return instance
                }
        }
    }
}
