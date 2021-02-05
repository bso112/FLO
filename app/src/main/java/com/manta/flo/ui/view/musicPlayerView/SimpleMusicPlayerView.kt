package com.manta.flo.ui.view.musicPlayerView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.annotation.MainThread
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.coroutineScope
import com.manta.flo.R
import com.manta.flo.utill.MusicPlayer
import com.manta.flo.utill.MusicPlayerListener
import com.manta.flo.ui.view.floSeekbar.FloSeekbar
import kotlinx.coroutines.*


class SimpleMusicPlayerView(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs),
    MusicPlayerListener,
    LifecycleObserver
{

    private val mView: View = View.inflate(context, R.layout.simple_music_player_view, this)
    private val mFloSeekbar : FloSeekbar = mView.findViewById<View>(R.id.flo_seekbar) as FloSeekbar
    private val mPlayButton = mView.findViewById<ImageButton>(R.id.btn_play)
    private var lifecycleCoroutineScope: LifecycleCoroutineScope? = null

    private val mPauseDrawble = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_pause_24, context.theme)
    private val mPlayDrawble = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_play_arrow_24, context.theme)


    var file_uri: String? = null
        set(value) {
            if (value != null)
                onSetMusic(value)
            field = value
        }


    init {
        initUI()
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

        MusicPlayer.ifMediaPlayerNotNull {
            if(it.isPlaying)
                onMusicStart()
        }
    }


    @MainThread
    private fun initUI() {
        //노래 재생
        mPlayButton.setOnClickListener {
            MusicPlayer.ifMediaPlayerNotNull {
                if (it.isPlaying)
                    MusicPlayer.pause()
                else
                    MusicPlayer.start()

            }
            switchPlayButtonImage()
        }


    }

    @MainThread
    private fun switchPlayButtonImage() {
        MusicPlayer.ifMediaPlayerNotNull {
            if (it.isPlaying)
                mPlayButton.background = mPauseDrawble
            else
                mPlayButton.background = mPlayDrawble
        }

    }

    private fun onSetMusic(fileUri: String) {
        MusicPlayer.setMusic(context, fileUri)
        mFloSeekbar.onMusicSet()
        switchPlayButtonImage()
    }


    override fun onMusicStart() {
        MusicPlayer.ifMediaPlayerNotNull {
            lifecycleCoroutineScope?.launch {
                while (it.isPlaying) {
                    if(!mFloSeekbar.isSeekBarPressed()){
                        mFloSeekbar.setProgress(it.currentPosition)
                    }
                        delay(1000)
                }
            }
        }
    }

    override fun onMusicChange() {

    }


    override fun onMusicPause() {
        super.onMusicPause()
        switchPlayButtonImage()
    }

    override fun onMusicSeekTo(ms: Int) {
        mFloSeekbar.setProgress(ms)
    }

    override fun onMusicEnd() {
        super.onMusicEnd()
        switchPlayButtonImage()
        MusicPlayer.seekTo(0)
    }





}




