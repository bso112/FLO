package com.manta.flo.ui.musicPlayer

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.MainThread
import com.manta.flo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class Lyric(
    val timeStampInMs: Int = 0,
    val lyric: String = ""
)

class LyricView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val mView: View = View.inflate(context, R.layout.lyric_view, this)
    private val mLyricQ: Queue<Lyric> = LinkedList<Lyric>()

    var lyric: String? = null
        set(value) {
            if (value != null) {
                parseLyric(value)
            }
            field = value;
        }


    suspend fun showLyric(timestamp: Int) {
        val front = mLyricQ.peek() ?: return
        if (front.timeStampInMs < timestamp) {
            withContext(Dispatchers.Main) {
                consumeLyric()
            }
        }
    }

    @MainThread
    private fun consumeLyric() {
        mView.findViewById<TextView>(R.id.tv_top).text = mLyricQ.poll()?.lyric
        mView.findViewById<TextView>(R.id.tv_down).text = mLyricQ.poll()?.lyric
    }

    private fun parseLyric(lyric: String) {
        val lines = lyric.split("\n")
        for (line in lines) {
            val info = line.split("]")
            val timestamp = parseInMs(info[0].substring(1))
            mLyricQ.add(Lyric(timestamp, info[1]))
        }
    }

    private fun parseInMs(timestamp: String): Int {
        val time = timestamp.split(":")
        val m = Integer.parseInt(time[0])
        val s = Integer.parseInt(time[1])
        val ms = Integer.parseInt(time[2])
        return m * 60 * 1000 + s * 1000 + ms;
    }
}