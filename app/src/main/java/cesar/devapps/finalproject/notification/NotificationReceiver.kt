package cesar.devapps.finalproject.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import cesar.devapps.finalproject.download.DownloadWorker
import java.util.concurrent.TimeUnit


class NotificationReceiver : BroadcastReceiver() {
   val TAG = NotificationReceiver::class.java.simpleName
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d("MyFirstBoot", "Aconteceu o boot completed")
            val workManager = WorkManager.getInstance()
            val periodicWorkRequest = PeriodicWorkRequest.Builder(NotificationWorker::class.java,1,TimeUnit.DAYS)
                .build()
            workManager.enqueue(periodicWorkRequest)
        } else {
            Log.e(TAG, "Aconteceu algum problema, Action ${intent?.action}")
        }
    }
}
