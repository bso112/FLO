package com.manta.flo.ui.musicPlayer

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.MainThread
import com.manta.flo.R
import com.manta.flo.ui.MusicPlayer

class BasicFloSeekbar(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs)
    , FloSeekbar {
    private val mView: View = View.inflate(context, R.layout.flo_seekbar, this)
    private val mSeekBar = mView.findViewById<SeekBar>(R.id.seekbar_music)
    private val mStartTextView = mView.findViewById<TextView>(R.id.tv_start)
    private val mEndTextView = mView.findViewById<TextView>(R.id.tv_end)
    private val mTimelineTextView = mView.findViewById<TextView>(R.id.tv_timeline)


    init {
        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val ratio = progress / mSeekBar.max.toFloat().coerceAtLeast(1F)
                var posX = (mSeekBar.width - mSeekBar.paddingRight - mSeekBar.paddingLeft) * ratio - (mTimelineTextView.width / 2)
                posX += mSeekBar.paddingLeft;

                mTimelineTextView.x = posX;
                mTimelineTextView.text = getTimeString(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mTimelineTextView.visibility = View.VISIBLE
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    mSeekBar.progress = seekBar.progress
                    MusicPlayer.seekTo(seekBar.progress)
                    mTimelineTextView.visibility = View.GONE

                }
            }

        })
    }


    override fun isSeekBarPressed() = mTimelineTextView.visibility == View.VISIBLE

    @MainThread
    override fun setProgress(ms: Int) {
        mSeekBar.progress = ms
        mStartTextView.text = getTimeString(ms)
    }

    @MainThread
    override fun onMusicSet(){
        MusicPlayer.ifMediaPlayerNotNull {
            mSeekBar.max = it.duration
            mSeekBar.progress = it.currentPosition
            mStartTextView.text = getTimeString(it.currentPosition)
            mEndTextView.text = getTimeString(it.duration)
        }
    }


}