package com.manta.flo.ui

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import com.manta.flo.R


class MusicPlayerView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val view: View = View.inflate(context, R.layout.music_player_view, this)
    var music_uri : String? = null;

    init {
        //노래 재생
        view.findViewById<ImageButton>(R.id.btn_play).setOnClickListener {
            music_uri?.let { MediaPlayer.create(context, Uri.parse(it)).start() }
        }
    }


}