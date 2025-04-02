package com.example.fastdictionary.model.room.dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
/**
 * Creator: Javohir Oromov
 * Project: FastDictionary
 * Date: 27/03/25
 * Javohir's MacBook Air
 */
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: T)
}