package org.lineageos.dspvolume.xiaomi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class VolumeListenerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!AudioManager.VOLUME_CHANGED_ACTION.equals(intent.getAction())
                || intent.getIntExtra(AudioManager.EXTRA_VOLUME_STREAM_TYPE, -1)
                        != AudioManager.STREAM_MUSIC) {
            return;
        }

        AudioManager audioManager = context.getSystemService(AudioManager.class);
        int current = intent.getIntExtra(AudioManager.EXTRA_VOLUME_STREAM_VALUE, -1);
        if (current < 0) {
            current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        audioManager.setParameters("volume_change=" + current + ";flags=8");
    }
}
