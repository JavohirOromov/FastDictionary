package com.example.fastdictionary.app

import android.app.Application
import com.example.fastdictionary.model.room.dataBase.MyDatabase
import com.example.fastdictionary.repository.repositoryImpl.AppRepositoryImpl

/**
 * Creator: Javohir Oromov
 * Project: FastDictionary
 * Date: 26/03/25
 * Javohir's MacBook Air
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        MyDatabase.init(this)
        val dictionaryDao = MyDatabase.database.dictionaryDao()
        AppRepositoryImpl.init(dictionaryDao)
    }
}