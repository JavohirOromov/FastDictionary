package com.example.fastdictionary.model.room.dao
import android.database.Cursor
import android.media.RouteListingPreference
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.fastdictionary.model.room.entity.DictionaryEntity
/**
 * Creator: Javohir Oromov
 * Project: FastDictionary
 * Date: 27/03/25
 * Javohir's MacBook Air
 */
@Dao
interface DictionaryDao: BaseDao<DictionaryEntity> {

    @Query("select * from dictionary where dictionary.uzbek like :st || '%' ")
    fun getSearchWordsUz(st: String): Cursor

    @Query("select * from dictionary where dictionary.english like :st || '%'  limit :limit")
    fun getSearchWordsEnLimit(st: String, limit: Int): List<DictionaryEntity>

    @Query("select * from dictionary where dictionary.english like :st || '%' ")
    fun getSearchWordsEn(st: String): Cursor

    @Query("select * from dictionary")
    fun getAllWords(): List<DictionaryEntity>

    @Query("SELECT * FROM dictionary")
    fun getWordsCursor(): Cursor

    @Query("SELECT * FROM dictionary WHERE is_favourite = 1")
    fun getFavorites(): Cursor

    @Query("UPDATE  dictionary SET is_favourite = :fav WHERE id = :id")
    fun updateFav(id: Int, fav: Int)


    @Query("SELECT * FROM dictionary WHERE id = :id")
    fun getWordById(id: Int): DictionaryEntity


}