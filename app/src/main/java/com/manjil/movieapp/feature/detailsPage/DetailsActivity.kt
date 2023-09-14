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
import com.manjil.movieapp.interfaces.ItemOnClickListener
import com.manjil.movieapp.model.DataItem
import java.io.Serializable

class DetailsActivity : AppCompatActivity(), ItemOnClickListener {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var viewModel: BaseViewModel
    private lateinit var dataItemList: ArrayList<DataItem>
    private var position: Int = 0
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

        position = intent.getIntExtra("position", 0)
        setMovieDescription()
        setRelatedMovies()
        setExoPlayer()
        binding.cvThumbnail.setOnClickListener {
            binding.cvThumbnail.visibility = View.GONE
            player.prepare()
            player.play()
        }
        checkPosition()

        ibPrev.setOnClickListener { previousButtonClicked() }
        ibNext.setOnClickListener { nextButtonClicked() }
        ivToggleFullscreen.setOnClickListener { fullscreenToggle() }
        ivPlayPause.setOnClickListener { playPauseVideo() }
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
        if (position < dataItemList.size - 1) openNewVideo(position + 1)
    }

    private fun previousButtonClicked() {
        if (position > 0) openNewVideo(position - 1)
    }

    private fun <T> getControllerViewById(id: Int): T {
        return binding.playerView.findViewById(id)
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun setExoPlayer() {
        player =
            ExoPlayer.Builder(this).setSeekForwardIncrementMs(10000).setSeekBackIncrementMs(10000)
                .build()
        binding.playerView.player = player

        val uri = Uri.parse(videoUrl)
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.addListener(object : Player.Listener {
            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo, newPosition: Player.PositionInfo, reason: Int
            ) {
                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                getControllerViewById<TextView>(R.id.tvCurrentDuration).text =
                    formatTime(player.currentPosition / 1000)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_READY -> {
//                        ivPlayPause.alpha = 1f
                        getControllerViewById<TextView>(R.id.tvTotalDuration).text = getString(
                            R.string.total_duration, formatTime(player.contentDuration / 1000)
                        )
                        handler.post(durationUpdateRunnable)
                    }

                    Player.STATE_ENDED -> {
                        binding.cvThumbnail.visibility = View.VISIBLE
                        player.seekTo(0)
                        player.pause()
                    }

                    else -> {
//                        ivPlayPause.alpha = 0f
                        handler.removeCallbacks(durationUpdateRunnable)
                        Log.d("handler1", "onPlaybackStateChanged: handler canceled")
                    }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                if (error.cause is HttpDataSourceException) {
                    binding.cvThumbnail.visibility = View.VISIBLE
                    Toast.makeText(
                        this@DetailsActivity,
                        "Connection timeout: Please check the internet connection.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private val durationUpdateRunnable = object : Runnable {
        override fun run() {
            if (player.playWhenReady) {
                getControllerViewById<TextView>(R.id.tvCurrentDuration).text =
                    formatTime(player.currentPosition / 1000)
                Log.d("handler", "run: durationUpdate")
            }
            handler.postDelayed(this, 1000)
        }
    }

    private fun checkPosition() {
        if (position == 0) {
            ibPrev.alpha = .4f
            ibPrev.isEnabled = false
        } else {
            ibPrev.alpha = 1f
            ibPrev.isEnabled = true
        }
        if (position == dataItemList.size - 1) {
            ibNext.alpha = .4f
            ibNext.isEnabled = false
        } else {
            ibNext.alpha = 1f
            ibNext.isEnabled = true
        }
    }

    private fun setRelatedMovies() {
        val adapter = RelatedMovieListAdapter(
            dataItemList.filterIndexed { index, _ -> index != position }, this, this
        )
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRelatedMovieContainer.layoutManager = layoutManager
        binding.rvRelatedMovieContainer.adapter = adapter
    }

    private fun setMovieDescription() {
        dataItemList = intent.serializable("movie")!!

        val data: DataItem = dataItemList[position]
        binding.tvMovieTitle.text = data.weather?.description
        binding.tvMovieRating.text = data.temp.toString()
        binding.tvMovieDuration.text = getString(R.string.minutes, data.windSpd.toString())
        binding.tvMovieSynopsis.text = getString(
            R.string.synopsis_detail, data.weather?.description, data.windCdir, data.windCdirFull
        ).repeat(10)

        Glide.with(this).load(thumbnailUrl).placeholder(R.drawable.img_placeholder)
            .into(binding.ivMovieThumbnail)
    }

    private fun fullscreenToggle() {
        val orientation = resources.configuration.orientation
        requestedOrientation =
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            else ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

//        Handler(Looper.getMainLooper()).postDelayed({
//            Log.d("handler1", "scalingButtonClicked: ")
//            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
//        }, 1000)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) hideSystemUI() else showSystemUI()
    }

    //hides the status bar and navigation bar
    private fun hideSystemUI() {
        getControllerViewById<ImageView>(R.id.ivToggleFullscreen).setImageResource(R.drawable.ic_fullscreen_exit)
        binding.scrollView.visibility = View.GONE

        @Suppress("DEPRECATION") if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    //shows hidden status bar and navigation bar
    private fun showSystemUI() {
        getControllerViewById<ImageView>(R.id.ivToggleFullscreen).setImageResource(R.drawable.ic_fullscreen)
        binding.scrollView.visibility = View.VISIBLE

        @Suppress("DEPRECATION") if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
            key, T::class.java
        )

        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    private fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60
        return if (hours > 0) String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
        else String.format("%02d:%02d", minutes, remainingSeconds)
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

    override fun onItemClick(dataItemList: List<DataItem?>?, position: Int) {
        if (position >= this.position) openNewVideo(position + 1) else openNewVideo(position)
    }

    private fun openNewVideo(position: Int) {
        val intent = Intent(baseContext, DetailsActivity::class.java)
        intent.putExtra("movie", dataItemList)
        intent.putExtra("position", position)
        startActivity(intent)
        finish()
    }
}