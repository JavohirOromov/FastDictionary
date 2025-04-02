package com.example.fastdictionary.screen.favourite
import MainViewModel
import MainViewModelImpl
import TextToSpeechHelper
import android.database.Cursor
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fastdictionary.R
import com.example.fastdictionary.adapter.MainAdapter
import com.example.fastdictionary.databinding.FragmentFavouriteBinding
import com.example.fastdictionary.dialog.DetailDialog
import com.example.fastdictionary.repository.repository.AppRepository
import com.example.fastdictionary.repository.repositoryImpl.AppRepositoryImpl
/**
 * Creator: Javohir Oromov
 * Project: FastDictionary
 * Date: 27/03/25
 * Javohir's MacBook Air
 */
class FavoriteFragment: Fragment(R.layout.fragment_favourite) {

    private val binding: FragmentFavouriteBinding by viewBinding(FragmentFavouriteBinding::bind)
    private val repository: AppRepository = AppRepositoryImpl.getInstance()
    private val adapter by lazy { MainAdapter() }
    private var addCursor: Cursor = repository.getAllFavourite()
    private val viewModel: MainViewModel by viewModels<MainViewModelImpl>()
    private val dialog: DetailDialog by lazy { DetailDialog(requireContext(),viewModel) }
    private lateinit var textToSpeech: TextToSpeechHelper
    private val args: FavoriteFragmentArgs by navArgs()
    private var isLanguage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.showDialogLiveDate.observe(this,showDialogObserver)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.speakTextLiveDate.observe(viewLifecycleOwner,textToSpeechObserver)
        addCursor = repository.getAllFavourite()
        isLanguage = args.isLanguage
        initAdapter()
        addClickEvents()
        checkAnimation()
        initTextToSpeech()
        updateAdapter()
    }

    private val showDialogObserver = Observer<Pair<Int,Int>>{ (id,fav) ->
        dialog.setId(id,fav)
        dialog.show()
    }
    private val textToSpeechObserver = Observer<String>{
        textToSpeech.speak(it)
    }
    private fun initAdapter(){
        binding.listItem.adapter = adapter
        binding.listItem.layoutManager = LinearLayoutManager(requireContext())
        adapter.swapLang(isLanguage)
        adapter.setCursor(addCursor)
    }
    private fun addClickEvents() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        adapter.setFavClickListener { id, fav ->
            repository.changeFavById(id, fav xor  1)
            val cursor = repository.getAllFavourite()
            addCursor = cursor
            adapter.setCursor(cursor)
            checkAnimation()
        }
        adapter.setItemClickListener { id, fav ->
            viewModel.showDialog(id,fav)
        }

    }
    private fun updateAdapter(){
        dialog.setOnFavoriteChangedListener { id, fav ->
            val cursor = repository.getAllFavourite()
            addCursor = cursor
            adapter.setCursor(cursor)
            checkAnimation()
        }
    }
    private fun initTextToSpeech() {
        textToSpeech = TextToSpeechHelper(requireContext()) { status ->
            when (status) {
                TextToSpeech.SUCCESS -> {

                }
                TextToSpeech.ERROR -> {
                }
            }
        }
    }
    private fun checkAnimation() {
        val isEmpty = when {
            addCursor.count == 0 -> true
            else -> false
        }
        if (isEmpty) {
            binding.animation.visibility = View.VISIBLE
            binding.listItem.visibility = View.GONE
            binding.animation.playAnimation()
        } else {
            binding.animation.visibility = View.GONE
            binding.listItem.visibility = View.VISIBLE
            binding.animation.pauseAnimation()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        addCursor.close()
    }
}