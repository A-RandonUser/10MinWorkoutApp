package com.example.workoutinminutes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [HistoryEntity::class], version = 2)
abstract class HistoryDatabase: RoomDatabase() {


    abstract fun historyDao():HistoryDao

    companion object {

        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        fun getInstance(context: Context): HistoryDatabase {

            synchronized(this) {
                var instance = INSTANCE

                //so basically wipes and rebuilds instead of migration
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HistoryDatabase::class.java, "history_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }

        }
    }
}