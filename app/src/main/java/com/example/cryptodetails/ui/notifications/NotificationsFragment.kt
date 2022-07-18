package com.example.cryptodetails.ui.notifications

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptodetails.MainActivity
import com.example.cryptodetails.R
import com.example.cryptodetails.app.CryptoApp
import com.example.cryptodetails.app.NotificationBroadcastReceiver
import com.example.cryptodetails.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var notificationManager: NotificationManagerCompat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        binding.viewModel = notificationsViewModel
        binding.lifecycleOwner = this@NotificationsFragment

        notificationManager = NotificationManagerCompat.from(requireContext())
        setupNotificationChannels()

        return binding.root
    }

    private fun setupNotificationChannels() {
        binding.sendOnChannel1.setOnClickListener {
            val contentIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_MUTABLE
            )
            val actionIntent = PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, NotificationBroadcastReceiver::class.java).putExtra(
                    NotificationBroadcastReceiver.TOAST_MESSAGE_KEY,
                    binding.notificationBody.text.toString()
                ),
                PendingIntent.FLAG_MUTABLE
            )

            val notificationChannel1 =
                NotificationCompat.Builder(requireContext(), CryptoApp.CHANNEL_1)
                    .setSmallIcon(R.drawable.error_loading_image)
                    .setContentTitle(binding.notificationTitle.text)
                    .setContentText(binding.notificationBody.text)
                    .setColor(Color.BLUE)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .addAction(R.mipmap.ic_launcher, "ToastMessage", actionIntent)
                    .build()

            notificationManager.notify(1, notificationChannel1)
        }

        binding.sendOnChannel2.setOnClickListener {
            val notificationChannel2 =
                NotificationCompat.Builder(requireContext(), CryptoApp.CHANNEL_2)
                    .setSmallIcon(R.drawable.error_loading_image)
                    .setContentTitle(binding.notificationTitle.text)
                    .setContentText(binding.notificationBody.text)
                    .setCategory(NotificationCompat.CATEGORY_ERROR)
                    .build()

            notificationManager.notify(2, notificationChannel2)
//                    .setCategory()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}