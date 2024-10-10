package com.example.project.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    private static MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        String mealType = intent.getStringExtra("mealType");

        // Hiển thị thông báo
        Toast.makeText(context, "Đã đến giờ " + mealType, Toast.LENGTH_LONG).show();

        // Phát âm thanh báo thức
        playRingtone(context);

//        // Khởi động Activity để hiển thị thông báo
//        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
//        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        alarmIntent.putExtra("mealType", mealType);
//        context.startActivity(alarmIntent);
    }

    private void playRingtone(Context context) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopRingtone() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
