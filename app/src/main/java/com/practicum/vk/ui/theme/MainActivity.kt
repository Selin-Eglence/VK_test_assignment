package com.practicum.vk.ui.theme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadOptions
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.*
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.practicum.vk.R

class MainActivity : AppCompatActivity() {
    private lateinit var castContext: CastContext
    private var castSession: CastSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main )

        castContext = CastContext.getSharedInstance(this)

        val castButton = findViewById<MediaRouteButton>(R.id.video)
        CastButtonFactory.setUpMediaRouteButton(this, castButton)

        // Добавляем слушатель сессий
        castContext.sessionManager.addSessionManagerListener(
            sessionListener, CastSession::class.java
        )
    }

    private val sessionListener = object : SessionManagerListener<CastSession> {
        override fun onSessionStarted(p0: CastSession, p1: String) {
            castSession
            playVideo()
        }

        override fun onSessionEnded(p0: CastSession, error: Int) {
            castSession = null
        }

        override fun onSessionResumed(p0: CastSession, wasSuspended: Boolean) {
            castSession
        }

        override fun onSessionSuspended(p0: CastSession, reason: Int) {}
        override fun onSessionStarting(p0: CastSession) {}
        override fun onSessionEnding(p0: CastSession) {}
        override fun onSessionResuming(p0: CastSession, p1: String) {}
        override fun onSessionStartFailed(p0: CastSession, error: Int) {}
        override fun onSessionResumeFailed(p0: CastSession, error: Int) {}
    }

    private fun playVideo() {
        castSession = castContext.sessionManager.currentCastSession
        castSession?.remoteMediaClient?.let { remoteMediaClient ->
            val mediaMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE).apply {
                putString(MediaMetadata.KEY_TITLE, "Test Video")
            }

            val mediaInfo = MediaInfo.Builder("https://videolink-test.mycdn.me/?pct=1&sig=6QNOvp0y3BE&ct=0&clientType=45&mid=193241622673&type=5")
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("video/mp4")
                .setMetadata(mediaMetadata)
                .build()

            remoteMediaClient.load(mediaInfo, MediaLoadOptions.Builder().build())
        }
    }
}
