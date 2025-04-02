package com.example.fastdictionary.repository.repository
import android.database.Cursor
import androidx.lifecycle.LiveData
import com.example.fastdictionary.model.room.entity.DictionaryEntity

/**
 * Creator: Javohir Oromov
 * Project: FastDictionary
 * Date: 27/03/25
 * Javohir's MacBook Air
 */
interface AppRepository {
    fun getCursor(): Cursor
    fun changeFavById(id: Int, fav: Int)
    fun getWordById(id: Int): DictionaryEntity
    fun update(dictionaryEntity: DictionaryEntity)
    fun getAllFavourite(): Cursor
    fun getSearchWordsUz(str: String): Cursor
    fun getSearchWordEng(str: String): Cursor
}