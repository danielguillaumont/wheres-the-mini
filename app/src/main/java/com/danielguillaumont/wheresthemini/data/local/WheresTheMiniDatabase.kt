package com.danielguillaumont.wheresthemini.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        ParkingEntity::class
    ],
    version = 2,
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

        private val MIGRATION_1_2 =
            object : Migration(
                1,
                2
            ) {

                override fun migrate(
                    database: SupportSQLiteDatabase
                ) {

                    database.execSQL(
                        """
                        ALTER TABLE parking_sessions
                        ADD COLUMN parkingExpiryMillis INTEGER
                        """.trimIndent()
                    )

                    database.execSQL(
                        """
                        ALTER TABLE parking_sessions
                        ADD COLUMN reminderEnabled INTEGER NOT NULL DEFAULT 0
                        """.trimIndent()
                    )
                }
            }

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
                            .addMigrations(
                                MIGRATION_1_2
                            )
                            .build()

                    INSTANCE = instance

                    instance
                }
        }
    }
}