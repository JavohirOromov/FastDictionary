package com.example.fastdictionary.screen.main
import MainViewModel
import MainViewModelImpl
import TextToSpeechHelper
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.fastdictionary.R
import com.example.fastdictionary.adapter.MainAdapter
import com.example.fastdictionary.databinding.FragmentMainBinding
import com.example.fastdictionary.dialog.DetailDialog
import java.util.Locale
/**
 * Creator: Javohir Oromov
 * Project: FastDictionary
 * Date: 26/03/25
 * Javohir's MacBook Air
 */
class MainFragment: Fragment(R.layout.fragment_main) {
    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels<MainViewModelImpl>()
    private val adapter: MainAdapter by lazy { MainAdapter() }
    private val dialog: DetailDialog by lazy { DetailDialog(requireContext(), viewModel) }
    private lateinit var textToSpeech: TextToSpeechHelper
    private var isLanguage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.openFavouriteLiveData.observe(this, openFavouriteObserver)
        viewModel.showDialogLiveDate.observe(this,showDialogObserver)
        viewModel.openInfoLiveDate.observe(this,openInfoObserver)
        viewModel.setVoiceSearchQueryLiveData.observe(this,setVoiceQueryObserver)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateItem(0,0)
        viewModel.speakTextLiveDate.observe(viewLifecycleOwner,textToSpeechObserver)
        viewModel.isEnglishLiveDate.observe(viewLifecycleOwner,isEnglishObserver)
        viewModel.leftTextLiveData.observe(viewLifecycleOwner,leftTextObserver)
        viewModel.rightTextLiveDate.observe(viewLifecycleOwner,rightTextObserver)
        viewModel.searchQueryLiveData.observe(viewLifecycleOwner,searchQueryObserver)
        viewModel.visibleEmptyAnimation.observe(viewLifecycleOwner,visibleEmptyAnimationObserver)
        viewModel.invisibleEmptyAnimation.observe(viewLifecycleOwner,invisibleEmptyAnimationObserver)
        initAdapter()
        addClickEvents()
        updateAdapter()
        initTextToSpeech()
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
    private fun updateAdapter(){
        dialog.setOnFavoriteChangedListener { id, fav ->
            viewModel.updateItem(id, fav,true)
            adapter.setCursor(viewModel.cursor)
        }
    }
    private fun initAdapter() {
        binding.listItem.adapter = adapter
        binding.listItem.layoutManager = LinearLayoutManager(requireContext())
        adapter.setCursor(viewModel.cursor)
    }

    private fun addClickEvents() {
        adapter.setFavClickListener { id, fav ->
            viewModel.updateItem(id, fav)
            adapter.setCursor(viewModel.cursor)
        }
        binding.all.setOnClickListener {
            viewModel.openFavourite()
        }
        adapter.setItemClickListener { id, fav ->
            viewModel.showDialog(id,fav)
        }
        binding.info.setOnClickListener {
            viewModel.openInfo()
        }
        binding.sync.setOnClickListener {
            viewModel.clickSync()
        }
        binding.seach.addTextChangedListener(object: MyTextWatcher{
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                  viewModel.searchQuery(it.toString())
                }
            }
        })
        binding.mic.setOnClickListener {
            startVoiceSearch()
        }
    }
    private fun startVoiceSearch(){
        val language = if (isLanguage) "en-US" else "uz-UZ"
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Gapiring...")
        }
        try {
            startActivityForResult(intent, REQUEST_CODE_VOICE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Ovozli qidiruv qo‘llab-quvvatlanmaydi!", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_VOICE && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!result.isNullOrEmpty()) {
                viewModel.setVoiceResult(result[0])
            }
        }
    }
    companion object {
        private const val REQUEST_CODE_VOICE = 100
    }
    private val openFavouriteObserver = Observer<Boolean> {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToFavoriteFragment(it))
    }
    private val showDialogObserver = Observer<Pair<Int,Int>>{ (id,fav) ->
        dialog.setId(id,fav)
        dialog.show()
    }
    private val textToSpeechObserver = Observer<String>{
        textToSpeech.speak(it)
    }
    private val openInfoObserver = Observer<Unit>{
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToInfoFragment())
    }
    private val isEnglishObserver = Observer<Boolean>{
        isLanguage = it
        adapter.swapLang(it)
    }
    private val leftTextObserver = Observer<String>{
        binding.eng.text = it
    }
    private val rightTextObserver = Observer<String>{
        binding.uzb.text = it
    }
    private val searchQueryObserver = Observer<Pair<Cursor,String>>{ (cursor,query) ->
        adapter.setCursor(cursor)
        adapter.setSearchQuery(query)

    }
    private val setVoiceQueryObserver = Observer<String>{ query ->
        Log.d("TTT","memoriylik")
        binding.seach.setText(query)
    }
    private val visibleEmptyAnimationObserver = Observer<Unit>{
        binding.animation.visibility = View.VISIBLE
        binding.listItem.visibility = View.GONE
    }
    private val invisibleEmptyAnimationObserver = Observer<Unit>{
        binding.animation.visibility = View.GONE
        binding.listItem.visibility = View.VISIBLE
    }
}
interface MyTextWatcher: TextWatcher{
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }
}