package diploma.college.diplomaproject;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class NotifyService extends Service
{
 final String LOG_TAG = "myLogs";

  public void onCreate()
  {
    super.onCreate();
    Log.d(LOG_TAG, "onCreate");
  }

  public int onStartCommand(Intent intent, int flags, int startId)
  {
    Log.d(LOG_TAG, "onStartCommand");
    someTask();
    return super.onStartCommand(intent, flags, startId);
  }

  public void onDestroy()
  {
    super.onDestroy();
    Log.d(LOG_TAG, "onDestroy");
  }

  public IBinder onBind(Intent intent)
  {
    Log.d(LOG_TAG, "onBind");
    return null;
  }

    void someTask()
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                for (int i = 1; i <= 10; i++)
                {
                    try
                    {
                        TimeUnit.SECONDS.sleep(1);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 200);
                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 600);

                stopSelf();
            }
        }).start();
    }
}
