package com.example.cryptodetails.ui.media

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptodetails.R
import com.example.cryptodetails.databinding.FragmentMediaPlayerBinding


class MediaPlayerFragment : Fragment() {

    private var _binding: FragmentMediaPlayerBinding? = null

    private val binding get() = _binding!!

    private var mediaPlayer: MediaPlayer? = null
    

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mediaPlayerViewModel = ViewModelProvider(this)[MediaPlayerViewModel::class.java]
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_media_player, container, false)
        binding.lifecycleOwner = this@MediaPlayerFragment
        binding.viewModel = mediaPlayerViewModel
        binding.play.setOnClickListener {
            play()
        }
        binding.pause.setOnClickListener {
            pause()
        }
        binding.stop.setOnClickListener {
            stop()
        }

        return binding.root
    }

    private fun play() {
        if (mediaPlayer == null) {
            val url =
                "https://file-examples.com/storage/fe522079b962f100d94fb66/2017/11/file_example_MP3_700KB.mp3" // your URL here

            mediaPlayer = MediaPlayer()
            mediaPlayer?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .setLegacyStreamType(AudioManager.STREAM_ALARM)
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            mediaPlayer?.setDataSource(url)
            mediaPlayer?.isLooping = true
            mediaPlayer?.prepare() // might take long! (for buffering, etc)
            mediaPlayer?.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
                override fun onCompletion(mp: MediaPlayer?) {

                }
            })
        }
        mediaPlayer?.start()

        Toast.makeText(requireContext(), "Playing the audio", Toast.LENGTH_LONG).show()
    }

    private fun pause() {
        mediaPlayer?.pause()
        Toast.makeText(requireContext(), "Paused the audio", Toast.LENGTH_LONG).show()
    }

    private fun stop() {
        stopPlayer()
    }

    private fun stopPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        Toast.makeText(requireContext(), "Stopped player", Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        stopPlayer()
    }

    companion object {

    }
}