package com.danielguillaumont.wheresthemini.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ParkingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WheresTheMiniDatabase :
    RoomDatabase() {

    abstract fun parkingDao():
            ParkingDao

    companion object {

        @Volatile
        private var INSTANCE:
                WheresTheMiniDatabase? = null

        fun getDatabase(
            context: Context
        ): WheresTheMiniDatabase {

            return INSTANCE
                ?: synchronized(this) {

                    val instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            WheresTheMiniDatabase::class.java,
                            "wheres_the_mini.db"
                        )
                            .build()

                    INSTANCE = instance

                    instance
                }
        }
    }
}