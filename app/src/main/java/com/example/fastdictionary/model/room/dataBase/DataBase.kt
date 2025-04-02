package com.example.fastdictionary.model.room.dataBase
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fastdictionary.model.room.dao.DictionaryDao
import com.example.fastdictionary.model.room.entity.DictionaryEntity
/**
 * Creator: Javohir Oromov
 * Project: FastDictionary
 * Date: 27/03/25
 * Javohir's MacBook Air
 */
@Database(entities = [DictionaryEntity::class], version = 1)
abstract class MyDatabase : RoomDatabase() {

    abstract fun dictionaryDao(): DictionaryDao

    companion object {
        lateinit var database: MyDatabase
            private set

        fun init(context: Context) {
            if (!Companion::database.isInitialized) database =
                Room.databaseBuilder(context, MyDatabase::class.java, "MyDatabase.db")
                    .createFromAsset("dictionary.db")
                    .allowMainThreadQueries()
                    .build()
        }
    }
}
