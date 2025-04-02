package com.example.fastdictionary.model.room.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
/**
 * Creator: Javohir Oromov
 * project: FastDictionary
 * Javohir's MacBook Air
 */
@Entity(tableName = "dictionary")
data class DictionaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val uzbek: String?,
    val english: String?,
    val type: String?,
    @ColumnInfo("is_favourite")
    var isFavorite: Int?,
    val countable: String?,
    val transcript: String?
)
