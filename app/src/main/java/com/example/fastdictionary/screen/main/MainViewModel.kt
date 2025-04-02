import android.database.Cursor
import androidx.lifecycle.LiveData

/**
 * Creator: Javohir Oromov
 * Project: FastDictionary
 * Date: 27/03/25
 * Javohir's MacBook Air
 */
interface MainViewModel {
    val cursor: Cursor
    val openFavouriteLiveData: LiveData<Boolean>
    val updateLiveData: LiveData<Cursor>
    val showDialogLiveDate: LiveData<Pair<Int,Int>>
    val speakTextLiveDate: LiveData<String>
    val openInfoLiveDate: LiveData<Unit>
    val isEnglishLiveDate: LiveData<Boolean>
    val leftTextLiveData: LiveData<String>
    val rightTextLiveDate: LiveData<String>
    val searchQueryLiveData: LiveData<Pair<Cursor,String>>
    val setVoiceSearchQueryLiveData: LiveData<String>
    val visibleEmptyAnimation: LiveData<Unit>
    val invisibleEmptyAnimation: LiveData<Unit>



    fun updateItem(id: Int, isFav: Int,isDialog: Boolean = false)
    fun openFavourite()
    fun showDialog(id: Int, fav: Int)
    fun speak(text: String)
    fun openInfo()
    fun clickSync()
    fun searchQuery(str: String)
    fun setVoiceResult(str: String)
}