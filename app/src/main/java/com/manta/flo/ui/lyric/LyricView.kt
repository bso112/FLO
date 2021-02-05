package com.manta.flo.ui.lyric

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.coroutineScope
import com.manta.flo.R
import com.manta.flo.utill.Lyric
import com.manta.flo.utill.MusicPlayer
import com.manta.flo.utill.MusicPlayerListener
import kotlinx.coroutines.*


class LyricView(context: Context, attrs: AttributeSet) :
    ScrollView(context, attrs),
    MusicPlayerListener,
    LifecycleObserver
{

    private val mLyricTextViews = mutableListOf<TextView>()
    private val mRoot = LinearLayout(context)
    private val COLOR_HIGHLIGHT = ResourcesCompat.getColor(resources, R.color.white, context.theme)
    private val COLOR_DEFAULT = ResourcesCompat.getColor(resources, R.color.gray, context.theme)
    private val LYRIC_HIGHLIGHT_BEFORE_MS = 500
    private val LYRIC_SCROLL_AFTER_MS = 200L
    private var lifecycleCoroutineScope : LifecycleCoroutineScope? = null


    //모든 객체에서 동일
    companion object {
        private val mLyrics = mutableListOf<Lyric>()
    }

    private var mNextLyricTimeStamp: Int = 0;
    private var mNextLyricIndex = -1;
    var mSelectMode = false
        private set

    var mMarginBtwLine = 0
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
                mMarginBtwLine = getDimensionPixelSize(R.styleable.LyricView2_marginBtwLine, 0)
            } finally {
                recycle()
            }
        }

        mRoot.orientation = LinearLayout.VERTICAL
        addView(mRoot)


    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        MusicPlayer.register(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        MusicPlayer.unRegister(this)
    }

    fun registerLifecycleOwner(lifecycle: Lifecycle){
        lifecycle.addObserver(this)
        lifecycleCoroutineScope = lifecycle.coroutineScope
    }



    /**
     * 사용자에 의해 다른 구간으로 넘어갔을때 사용
     */
    private fun jumpLyricTo(timestamp: Int) {
        if (mLyrics.isEmpty()) return;
        MusicPlayer.ifMediaPlayerNotNull {
            if (timestamp > it.duration ||
                timestamp < mLyrics[0].timeStampInMs
            ) {
                setLyric(-1)
                return@ifMediaPlayerNotNull
            }
            setLyric(findLyricIndex(timestamp), false);
        }
    }

    /**
     * 순차적으로 음악이 플레이 될때 사용
     */
    private suspend fun showLyric(timestamp: Int) {
        if (timestamp < mNextLyricTimeStamp - LYRIC_HIGHLIGHT_BEFORE_MS)
            return;

        withContext(Dispatchers.Main) {
            setLyric(mNextLyricIndex)
        }
    }

    fun setSelectMode() {
        mSelectMode = !mSelectMode
    }


    @MainThread
    private fun setLyric(index: Int, isSmooth: Boolean = true) {
        if (mLyricTextViews.isEmpty() || mLyrics.isEmpty())
            return

        for (tv in mLyricTextViews) {
            tv.setTextColor(COLOR_DEFAULT)
        }

        if (index >= 0) {
            mLyricTextViews[index].text = mLyrics[index].lyric
            mLyricTextViews[index].setTextColor(COLOR_HIGHLIGHT)


            lifecycleCoroutineScope?.launch {
                if (isNotLyricPreview())
                    scrollToView(mLyricTextViews[index], isSmooth)
                else
                    scrollToView2(mLyricTextViews[index], isSmooth)
            }
        }

        setNextLyric(index + 1)

    }


    private suspend fun scrollToView2(view: View, isSmooth: Boolean) {
        if (isSmooth) {
            delay(LYRIC_SCROLL_AFTER_MS)
            smoothScrollTo(0, view.y.toInt())
        } else
            scrollTo(0, view.y.toInt())
    }

    private suspend fun scrollToView(view: View, isSmooth: Boolean) {
        if (isSmooth)
            delay(LYRIC_SCROLL_AFTER_MS)

        var mat = DisplayMetrics()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(mat);

        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

        var statusBarOffsetY = 0
        if (resourceId > 0) {
            statusBarOffsetY = resources.getDimensionPixelSize(resourceId)
        }

        //(view.y + y) : view의 절대위치 (LyricView의 부모가 rootView일 경우)
        //y : LyricView의 왼쪽위 모서리 위치 즉, 0 ~ y 만큼의 길이
        //(view.y + y) - (mat.heightPixels / 2) : 현재 텍스트뷰 위치 ~ 스크린의 절반지점만큼의 길이
        //statusBarOffsetY : 상태바의 y 길이
        val scrollY = Math.max(0, ((view.y + y) - (mat.heightPixels / 2) + statusBarOffsetY).toInt());

        if (isSmooth)
            smoothScrollTo(0, scrollY)
        else
            scrollTo(0, scrollY)
    }

    private fun setNextLyric(nextIndex: Int) {
        val next = Math.min(mLyrics.size - 1, nextIndex)
        mNextLyricIndex = next;
        mNextLyricTimeStamp = mLyrics[next].timeStampInMs;
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
        if (mLyrics.isEmpty()) {
            //가사 파싱해서 저장하기
            parseLyric(lyric)
        }
        //가사 텍스트뷰 생성하기
        createLyricTextViews()


        MusicPlayer.ifMediaPlayerNotNull {
            onMusicStart()
            jumpLyricTo(it.currentPosition)
        }
    }


    private fun createLyricTextViews() {
        mMaxVisible = if (mMaxVisible == 0) mLyrics.size else mMaxVisible
        repeat(mLyrics.size) { i ->
            val textView = View.inflate(context, R.layout.item_lyric, null) as TextView
            textView.text = mLyrics[i].lyric
            textView.textAlignment = textAlignment
            textView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).also {
                it.setMargins(0, 0, 0, mMarginBtwLine)
            }

            if (isNotLyricPreview()) {
                textView.setOnClickListener {
                    if (mSelectMode) {
                        setLyric(i)
                        MusicPlayer.seekTo(mLyrics[i].timeStampInMs)
                    }
                }
            }
            mRoot.addView(textView)
            mLyricTextViews.add(textView)
        }

    }


    private fun isNotLyricPreview() = mMaxVisible >= mLyrics.size

    override fun onMusicStart() {
        super.onMusicStart()
        MusicPlayer.ifMediaPlayerNotNull {
            lifecycleCoroutineScope?.launch {
                while (it.isPlaying) {
                    showLyric(it.currentPosition)
                    delay(500)
                }
            }
        }
    }

    override fun onMusicSeekTo(ms: Int) {
        super.onMusicSeekTo(ms)
        jumpLyricTo(ms)
    }

    override fun onMusicChange() {
        super.onMusicChange()
        mLyrics.clear()
    }
}