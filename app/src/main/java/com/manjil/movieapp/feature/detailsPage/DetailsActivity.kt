package com.manjil.movieapp.feature.detailsPage

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.HttpDataSource.HttpDataSourceException
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.manjil.movieapp.BaseViewModel
import com.manjil.movieapp.R
import com.manjil.movieapp.databinding.ActivityDetailsBinding
import com.manjil.movieapp.model.DataItem
import com.manjil.movieapp.model.MoviePojo
import java.io.Serializable

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var viewModel: BaseViewModel
    private lateinit var data: DataItem
    private lateinit var player: ExoPlayer
    private lateinit var ibPrev: ImageButton
    private lateinit var ibNext: ImageButton
    private lateinit var ivToggleFullscreen: ImageView
    private lateinit var ivPlayPause: ImageView
    private val handler = Handler(Looper.getMainLooper())
    private var wasPlaying = false
    private val videoUrl =
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    private val thumbnailUrl =
        "https://storage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[BaseViewModel::class.java]
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ibNext = getControllerViewById(R.id.ibNext)
        ibPrev = getControllerViewById(R.id.ibPrev)
        ivToggleFullscreen = getControllerViewById(R.id.ivToggleFullscreen)
        ivPlayPause = getControllerViewById(R.id.ivPlayPause)

        setMovieDescription()
        setRelatedMovies()
        setExoPlayer()
        binding.cvThumbnail.setOnClickListener {
            binding.cvThumbnail.visibility = View.GONE
            player.play()
        }
        checkPosition()

        ibPrev.setOnClickListener { previousButtonClicked() }
        ibNext.setOnClickListener { nextButtonClicked() }
        ivToggleFullscreen.setOnClickListener { fullscreenToggle() }
        ivPlayPause.setOnClickListener{ playPauseVideo()}
    }

    private fun playPauseVideo() {
        if (player.isPlaying) {
            player.pause()
            ivPlayPause.setImageResource(R.drawable.ic_play)
        } else {
            player.play()
            ivPlayPause.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun nextButtonClicked() {
        val position = player.currentMediaItemIndex
        if (position > 0) {
            player.seekTo(position - 1, 0)
        }
        checkPosition()
    }

    private fun previousButtonClicked() {
        val position = player.currentMediaItemIndex
        if (position > 0) {
            player.seekTo(position - 1, 0)
        }
        checkPosition()
    }

    private fun <T> getControllerViewById(id: Int): T {
        return binding.playerView.findViewById(id)
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun setExoPlayer() {
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player

        val uri = Uri.parse(videoUrl)
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.addListener(
            object : Player.Listener {
                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                    super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                    getControllerViewById<TextView>(R.id.tvCurrentDuration).text =
                        formatTime(player.currentPosition / 1000)
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    if (playbackState == Player.STATE_READY) {
                        ivPlayPause.alpha = 1f
                        getControllerViewById<TextView>(R.id.tvTotalDuration).text =
                            getString(
                                R.string.total_duration,
                                formatTime(player.contentDuration / 1000)
                            )
                        handler.post(durationUpdateRunnable)
                    } else {
                        ivPlayPause.alpha = 0f
                        handler.removeCallbacks(durationUpdateRunnable)
                        Log.d("handler1", "onPlaybackStateChanged: handler cancled")
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    if (error.cause is HttpDataSourceException) {
                        Toast.makeText(
                            this@DetailsActivity,
                            "Connection timeout: Please check the internet connection.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }

    private val durationUpdateRunnable = object : Runnable {
        override fun run() {
            if (player.playWhenReady) {
                getControllerViewById<TextView>(R.id.tvCurrentDuration).text =
                    formatTime(player.currentPosition / 1000)
                Log.d("handler1", "run: durationupdate")
            }
            handler.postDelayed(this, 1000)
        }
    }

    private fun checkPosition() {
        if (player.currentMediaItemIndex == 0) {
            ibPrev.alpha = .4f
            ibPrev.isEnabled = false
        } else {
            ibPrev.alpha = 1f
            ibPrev.isEnabled = true
        }
        if (player.currentMediaItemIndex == player.mediaItemCount - 1) {
            ibNext.alpha = .4f
            ibNext.isEnabled = false
        } else {
            ibNext.alpha = 1f
            ibNext.isEnabled = true
        }
    }

    private fun setRelatedMovies() {
        viewModel.getMovieList()
        viewModel.movieList.observe(this, Observer {
            setMovieListAdapter(it)
        })
    }

    private fun setMovieDescription() {
        data = intent.serializable("movie")!!
        binding.tvMovieTitle.text = data.weather?.description
        binding.tvMovieRating.text = data.temp.toString()
        binding.tvMovieDuration.text = getString(R.string.minutes, data.windSpd.toString())
        binding.tvMovieSynopsis.text = getString(
            R.string.synopsis_detail,
            data.weather?.description,
            data.windCdir,
            data.windCdirFull
        ).repeat(10)

        Glide.with(this).load(thumbnailUrl).into(binding.ivMovieThumbnail)
    }

    private fun setMovieListAdapter(relatedMovieList: ArrayList<MoviePojo>?) {
        val adapter = RelatedMovieListAdapter(relatedMovieList!!)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRelatedMovieContainer.layoutManager = layoutManager
        binding.rvRelatedMovieContainer.adapter = adapter
    }


    private fun fullscreenToggle() {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            showSystemUI()
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            hideSystemUI()
        }

//        Handler(Looper.getMainLooper()).postDelayed({
//            Log.d("handler1", "scalingButtonClicked: ")
//            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
//        }, 1000)

    }

    //hides the status bar and navigation bar
    private fun hideSystemUI() {
        getControllerViewById<ImageView>(R.id.ivToggleFullscreen).setImageResource(R.drawable.ic_fullscreen_exit)
        binding.scrollView.visibility = View.GONE
        Log.d("fullscreen", "hideSystemUI: ")

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
        }
    }

    //shows hidden status bar and navigation bar
    private fun showSystemUI() {
        getControllerViewById<ImageView>(R.id.ivToggleFullscreen).setImageResource(R.drawable.ic_fullscreen)
        binding.scrollView.visibility = View.VISIBLE

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
            key,
            T::class.java
        )

        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    private fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60
        return if (hours > 0)
            String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
        else
            String.format("%02d:%02d", minutes, remainingSeconds)
    }

    override fun onResume() {
        super.onResume()
        if (wasPlaying) player.play()
    }

    override fun onPause() {
        super.onPause()
        wasPlaying = player.isPlaying
        if (wasPlaying) player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        wasPlaying = false
        player.clearMediaItems()
        player.release()
    }
}