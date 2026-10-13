package org.lineageos.dspvolume.xiaomi;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class VolumeListenerService extends Service {
    private final VolumeListenerReceiver volumeReceiver = new VolumeListenerReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        // This protected broadcast is sent by the system audio service.
        registerReceiver(volumeReceiver, new IntentFilter(AudioManager.VOLUME_CHANGED_ACTION),
                RECEIVER_EXPORTED);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AudioManager audioManager = getSystemService(AudioManager.class);
        int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setParameters("volume_change=" + current + ";flags=8");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(volumeReceiver);
        super.onDestroy();
    }
}
