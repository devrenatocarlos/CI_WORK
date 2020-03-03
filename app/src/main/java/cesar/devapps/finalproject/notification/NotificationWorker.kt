package cesar.devapps.finalproject.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import cesar.devapps.finalproject.R
import cesar.devapps.finalproject.views.LoginActivity
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NotificationWorker(context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {
    val TAG = NotificationWorker::class.java.simpleName

    override fun doWork(): Result {
        val context = this
        val CHANNEL_ID = "MYAPP"

        val notificationManager =
            context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context.applicationContext, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pedingIntent = PendingIntent.getActivity(
            context.applicationContext, 1234,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context.applicationContext, CHANNEL_ID)
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setContentTitle("Android Avançado lembrete")
        builder.setContentText("faça login para registrar suas atividades")
        builder.setPriority(NotificationCompat.PRIORITY_HIGH)
        builder.setAutoCancel(true)
        builder.setContentIntent(pedingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "MYAPP",
                "Android avançado", NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = builder.build()
        notificationManager.notify(1234, notification)

        return Result.success()
    }


}
