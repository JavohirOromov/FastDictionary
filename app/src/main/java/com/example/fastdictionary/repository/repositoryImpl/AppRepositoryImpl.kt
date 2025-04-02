package com.example.fastdictionary.repository.repositoryImpl
import android.database.Cursor
import com.example.fastdictionary.model.room.dao.DictionaryDao
import com.example.fastdictionary.model.room.entity.DictionaryEntity
import com.example.fastdictionary.repository.repository.AppRepository

/**
 * Creator: Javohir Oromov
 * Project: FastDictionary
 * Date: 27/03/25
 * Javohir's MacBook Air
 */
class AppRepositoryImpl(private val dictionaryDao: DictionaryDao): AppRepository {

    companion object{
        private lateinit var instance: AppRepository

        fun init(dictionaryDao: DictionaryDao){
            if (!Companion::instance.isInitialized){
                instance = AppRepositoryImpl(dictionaryDao)
            }
        }
        fun getInstance(): AppRepository{
            return instance
        }
    }

    override fun getCursor(): Cursor {
       return dictionaryDao.getWordsCursor()
    }

    override fun changeFavById(id: Int, fav: Int) {
        dictionaryDao.updateFav(id,fav)
    }

    override fun getWordById(id: Int): DictionaryEntity {
        return dictionaryDao.getWordById(id)
    }

    override fun update(dictionaryEntity: DictionaryEntity) {
        dictionaryDao.insert(dictionaryEntity)
    }

    override fun getAllFavourite(): Cursor{
       return dictionaryDao.getFavorites()
    }

    override fun getSearchWordsUz(str: String): Cursor {
        return dictionaryDao.getSearchWordsUz(str)
    }

    override fun getSearchWordEng(str: String): Cursor {
        return dictionaryDao.getSearchWordsEn(str)
    }
}