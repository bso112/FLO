package com.manta.flo.ui.musicPlayer

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.core.content.res.ResourcesCompat
import com.manta.flo.R
import com.manta.flo.ui.Lyric
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LyricView2(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val mLyricTextViews = mutableListOf<TextView>()

    private val COLOR_HIGHLIGHT = ResourcesCompat.getColor(resources, R.color.white, context.theme)
    private val COLOR_DEFAULT = ResourcesCompat.getColor(resources, R.color.gray, context.theme)

    //모든 객체에서 동일
    companion object {
        private val mLyrics = mutableListOf<Lyric>()
        private var mNextLyricTimeStamp: Int = 0;
        private var mNextLyricIndex = 0;
    }

    var mMaxVisible: Int = 0

    var lyric: String? = null
        set(value) {
            if (value != null) {
                onMusicSet(value)
            }
            field = value;
        }


    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LyricView2,
            0, 0
        ).apply {

            try {
                mMaxVisible = getInteger(R.styleable.LyricView2_maxVisible, 0)
            } finally {
                recycle()
            }
        }

        orientation = VERTICAL
        gravity = Gravity.CENTER
    }

    /**
     * 사용자에 의해 다른 구간으로 넘어갔을때 사용
     */
    fun jumpLyricTo(timestamp: Int) {
        val index = findLyricIndex(timestamp)
        setLyric(index);
    }

    /**
     * 순차적으로 음악이 플레이 될때 사용
     */
    suspend fun showLyric(timestamp: Int) {
        if (timestamp < mNextLyricTimeStamp)
            return;

        withContext(Dispatchers.Main) {
            setLyric(mNextLyricIndex)
        }
    }


    @MainThread
    private fun setLyric(index: Int) {
        if (mLyricTextViews.isEmpty()) return

        for (tv in mLyricTextViews) {
            tv.setTextColor(COLOR_DEFAULT)
        }
        mLyricTextViews[index % mMaxVisible].text = mLyrics[index].lyric
        mLyricTextViews[index % mMaxVisible].setTextColor(COLOR_HIGHLIGHT)
        setNextLyric(index + 1)

    }

    private fun findLyricIndex(timestamp: Int): Int {
        //binary search
        var start = 0;
        var end = mLyrics.size - 1;

        while (start <= end) {
            val mid = (end + start) / 2;
            if (mLyrics[mid].timeStampInMs > timestamp) {
                end = mid - 1;
            } else if (mLyrics[mid].timeStampInMs < timestamp) {
                start = mid + 1;
            } else {
                //찾았으면
                return mid;
            }
        }

        //못찾았다. timestamp보다 한단계 작은 원소의 인덱스를 리턴한다.
        return Math.max(end, 0);
    }


    private fun parseLyric(lyric: String) {
        val lines = lyric.split("\n")
        for (line in lines) {
            val info = line.split("]")
            val timestamp = parseInMs(info[0].substring(1))
            val newLyric = Lyric(timestamp, info[1]);
            mLyrics.add(newLyric)
        }
    }

    private fun parseInMs(timestamp: String): Int {
        val time = timestamp.split(":")
        val m = Integer.parseInt(time[0])
        val s = Integer.parseInt(time[1])
        val ms = Integer.parseInt(time[2])
        return m * 60 * 1000 + s * 1000 + ms;
    }

    private fun onMusicSet(lyric: String) {
        if(mLyrics.isEmpty()){
            //가사 파싱해서 저장하기
            parseLyric(lyric)
        }
        //가사 텍스트뷰 생성하기
        createLyricTextViews()
    }

    private fun createLyricTextViews() {
        mMaxVisible = if (mMaxVisible == 0) mLyrics.size else mMaxVisible
        for (i in 0 until mMaxVisible) {
            val textView = View.inflate(context, R.layout.item_lyric, null) as TextView
            textView.text = mLyrics[i].lyric
            addView(textView)
            mLyricTextViews.add(textView)
        }
    }

    private fun setNextLyric(nextIndex: Int) {
        val next = Math.min(mLyrics.size - 1, nextIndex)
        mNextLyricIndex = next;
        mNextLyricTimeStamp = mLyrics[next].timeStampInMs;
    }

}