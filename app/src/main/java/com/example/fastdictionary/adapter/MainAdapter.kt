package com.example.fastdictionary.adapter
import android.database.Cursor
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.getColumnIndex
import com.example.fastdictionary.R
import com.example.fastdictionary.databinding.ItemMainBinding
import java.sql.Blob

/**
 * Creator: Javohir Oromov
 * Project: FastDictionary
 * Date: 26/03/25
 * Javohir's MacBook Air
 */
class MainAdapter : RecyclerView.Adapter<MainAdapter.VH>() {
    private var cursor: Cursor? = null
    private var isEnglish: Boolean = true
    private var searchQuery: String = ""

    private var favClickListener: ((Int, Int) -> Unit)? = null
    private var itemClickListener:((Int, Int) -> Unit)? = null


    fun setFavClickListener(favClickListener: (Int,Int) -> Unit){
        this.favClickListener = favClickListener
    }

    fun setItemClickListener(itemClickListener: (Int,Int) -> Unit){
        this.itemClickListener = itemClickListener
    }

    fun swapLang(isEnglish: Boolean){
        this.isEnglish = isEnglish
        notifyDataSetChanged()
    }
    fun setSearchQuery(query: String){
        this.searchQuery = query
    }
    fun setCursor(cursor: Cursor){
        this.cursor = cursor
        notifyDataSetChanged()
    }

    inner class VH(private val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            cursor?.let {
                it.moveToPosition(adapterPosition)
                val word = it.getString(it.getColumnIndex(if (isEnglish) "english" else "uzbek") ?: 0)
                binding.words.text = highlightText(word,searchQuery)

                val isFav = it.getInt(it.getColumnIndex("is_favourite")?: 0)

                binding.favorite.setImageResource(if (isFav == 1) R.drawable.star else R.drawable.favourite)
            }
        }



        init {
            binding.favorite.setOnClickListener {
                cursor?.let {
                    it.moveToPosition(adapterPosition)
                    favClickListener?.invoke(
                        it.getInt(it.getColumnIndex("id")?:0),
                        it.getInt(it.getColumnIndex("is_favourite")?:0)
                    )
                }
            }
            binding.root.setOnClickListener {
              cursor?.let {
                  it.moveToPosition(adapterPosition)
                  itemClickListener?.invoke(
                      it.getInt(it.getColumnIndex("id")?: 0),
                      it.getInt(it.getColumnIndex("is_favourite")?: 0)
                  )
              }
            }
        }
    }
    private fun highlightText(fullText: String, query: String?): SpannableString {
        val spannable = SpannableString(fullText)
        if (!query.isNullOrEmpty()) {
            val startIndex = fullText.lowercase().indexOf(query.lowercase())
            if (startIndex != -1) {
                val endIndex = startIndex + query.length
                spannable.setSpan(
                    ForegroundColorSpan(Color.RED), // Qizil rang
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD), // Qalin qilish
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannable
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = cursor?.count ?: 0
}
