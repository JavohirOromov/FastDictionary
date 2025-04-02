package com.example.fastdictionary.dialog
import MainViewModel
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.example.fastdictionary.R
import com.example.fastdictionary.databinding.DialogDetailBinding
import com.example.fastdictionary.model.room.entity.DictionaryEntity
import com.example.fastdictionary.repository.repository.AppRepository
import com.example.fastdictionary.repository.repositoryImpl.AppRepositoryImpl

class DetailDialog(context: Context,private val viewModel: MainViewModel) : AlertDialog(context) {
    private val binding: DialogDetailBinding = DialogDetailBinding.inflate(LayoutInflater.from(context))
    private val repository: AppRepository = AppRepositoryImpl.getInstance()
    private var id = 0
    private var fav = 0
    private var data: DictionaryEntity? = null

  private  var onFavoriteChangedListener: ((Int, Int) -> Unit)? = null
    fun setOnFavoriteChangedListener(onFavoriteChangedListener: (Int,Int) -> Unit){
        this.onFavoriteChangedListener = onFavoriteChangedListener
    }

    init {
        setView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.attributes?.windowAnimations = R.style.CustomDialogAnim
        setCancelable(false)

        binding.close.setOnClickListener { dismiss() }

        binding.favorite.setOnClickListener {
            val newFav = if (fav == 1) 0 else 1
            repository.changeFavById(id, newFav)
            fav = newFav
            updateFavoriteIcon()
            onFavoriteChangedListener?.invoke(id, newFav)
        }
        binding.sound.setOnClickListener {
            data?.english?.let {
                viewModel.speak(it)
            }
        }
    }
    fun setId(id: Int, fav: Int) {
        this.id = id
        this.fav = fav
        updateFavoriteIcon()
        loadData()
    }
    private fun updateFavoriteIcon() {
        val currentFav = repository.getWordById(id)
        binding.favorite.setImageResource(
            if (currentFav.isFavorite == 1) R.drawable.star else R.drawable.favourite
        )
    }
    private fun loadData() {
        data = repository.getWordById(id)
        data?.let { entity ->
            binding.engWord.text = entity.english
            binding.type.text = entity.type
            binding.countableWord.text = entity.countable.orEmpty().ifEmpty { "-" }
            binding.transcriptWord.text = entity.transcript
            binding.uzbWord.text = entity.uzbek
            updateFavoriteIcon()
        } ?: run {

        }
    }
}