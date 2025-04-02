import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fastdictionary.repository.repository.AppRepository
import com.example.fastdictionary.repository.repositoryImpl.AppRepositoryImpl


/**
 * Creator: Javohir Oromov
 * Project: FastDictionary
 * Date: 27/03/25
 * Javohir's MacBook Air
 */
 class MainViewModelImpl : MainViewModel, ViewModel() {
    private val repository: AppRepository = AppRepositoryImpl.getInstance()
    private var searchQuery = ""

    override var cursor: Cursor = repository.getCursor()
    override val openFavouriteLiveData = MutableLiveData<Boolean>()
    override val updateLiveData = MutableLiveData<Cursor>()
    override val showDialogLiveDate = MutableLiveData<Pair<Int, Int>>()
    override val speakTextLiveDate = MutableLiveData<String>()
    override val openInfoLiveDate = MutableLiveData<Unit>()
    override val isEnglishLiveDate = MutableLiveData(true)
    override val leftTextLiveData = MutableLiveData<String>()
    override val rightTextLiveDate = MutableLiveData<String>()
    override val searchQueryLiveData = MutableLiveData<Pair<Cursor, String>>()
    override val setVoiceSearchQueryLiveData = MutableLiveData<String>()
    override val visibleEmptyAnimation = MutableLiveData<Unit>()
    override val invisibleEmptyAnimation = MutableLiveData<Unit>()

    override fun updateItem(id: Int, isFav: Int, isDialog: Boolean) {
        if (!isDialog) repository.changeFavById(id, isFav xor 1)
        if (searchQuery.isEmpty()) {
            cursor = repository.getCursor()
            updateLiveData.value = cursor
        } else {
            if (isEnglishLiveDate.value == true) {
                cursor = repository.getSearchWordEng(searchQuery)
                searchQueryLiveData.value = Pair(cursor, searchQuery)
            } else {
                cursor = repository.getSearchWordsUz(searchQuery)
                searchQueryLiveData.value = Pair(cursor, searchQuery)
            }
        }
    }

    override fun openFavourite() {
        openFavouriteLiveData.value = isEnglishLiveDate.value
    }

    override fun showDialog(id: Int, fav: Int) {
        showDialogLiveDate.value = Pair(id, fav)
    }

    override fun speak(text: String) {
        speakTextLiveDate.value = text
    }

    override fun openInfo() {
        openInfoLiveDate.value = Unit
    }

    override fun clickSync() {
        isEnglishLiveDate.value = !isEnglishLiveDate.value!!
        if (isEnglishLiveDate.value!!) {
            leftTextLiveData.value = "Eng"
            rightTextLiveDate.value = "Uzb"
        } else {
            rightTextLiveDate.value = "Eng"
            leftTextLiveData.value = "Uzb"
        }
    }

    override fun searchQuery(str: String) {
        searchQuery = str
        if (isEnglishLiveDate.value == true) {
            cursor = repository.getSearchWordEng(str)
            searchQueryLiveData.value = Pair(cursor, searchQuery)
            if (cursor.count == 0) {
                visibleEmptyAnimation.value = Unit
            } else {
                invisibleEmptyAnimation.value = Unit
            }
        } else {
            cursor = repository.getSearchWordsUz(str)
            searchQueryLiveData.value = Pair(cursor, searchQuery)
            if (cursor.count == 0) {
                visibleEmptyAnimation.value = Unit
            } else {
                invisibleEmptyAnimation.value = Unit
            }
        }
    }

    override fun setVoiceResult(str: String) {
        if (isEnglishLiveDate.value == true) {
            cursor = repository.getSearchWordsUz(str)
            searchQueryLiveData.value = Pair(cursor, searchQuery)
            setVoiceSearchQueryLiveData.value = str
        } else {
            cursor = repository.getSearchWordEng(str)
            searchQueryLiveData.value = Pair(cursor, searchQuery)
            setVoiceSearchQueryLiveData.value = str
        }
    }
}