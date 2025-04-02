import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale
/**
 * Creator: Javohir Oromov
 * Project: FastDictionary
 * Date: 29/03/25
 * Javohir's MacBook Air
 */
class TextToSpeechHelper(private val context: Context, private val onInitListener: (Int) -> Unit) {
    private var textToSpeech: TextToSpeech? = null

    init {
        initializeTTS()
    }

    private fun initializeTTS(){
        textToSpeech = TextToSpeech(context){ status ->
            if (status == TextToSpeech.SUCCESS){
                val result = textToSpeech?.setLanguage(Locale.ENGLISH)


                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Til qo'llab-quvvatlanmaydi")
                }
            } else {
                Log.e("TTS", "Initialization failed")
            }
            onInitListener(status)
            }
        }
    fun speak(text: String) {
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun setLanguage(locale: Locale): Boolean {
        return textToSpeech?.setLanguage(locale) == TextToSpeech.LANG_COUNTRY_AVAILABLE
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }
    }