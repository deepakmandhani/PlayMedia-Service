package deepak.com.perpulesound.activity;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import deepak.com.perpulesound.model.Data;

public class SoundService extends Service implements MediaPlayer.OnPreparedListener {

    private MediaPlayer mediaPlayer;
    private MediaPlayer nextMediaPlayer;
    private List<Data> dataList;
    private IBinder iBinder = new SoundBinder();

    public class SoundBinder extends Binder {
        SoundService getSoundService() {
            return SoundService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = getMediaPlayer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (mediaPlayer.equals(this.mediaPlayer)) {
            mediaPlayer.start();
            Log.d("SoundService", "PLayting 127");

        }
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }

    private MediaPlayer getMediaPlayer() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();
        mediaPlayer.setAudioAttributes(audioAttributes);
        return mediaPlayer;
    }

    public void playSound(int soundNumber) {
        if (nextMediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = nextMediaPlayer;
            mediaPlayer.start();
            Log.d("SoundService", "PLayting 73");
        } else {
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(dataList.get(soundNumber).getAudio());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.prepareAsync();
            Log.d("SoundService", "prepare  82");
        }
        if (++soundNumber < dataList.size())
            prepareNextSound(soundNumber);

    }

    public void prepareNextSound(int soundNumber) {
        try {
            nextMediaPlayer = getMediaPlayer();
            nextMediaPlayer.setDataSource(dataList.get(soundNumber).getAudio());
        } catch (IOException e) {
            e.printStackTrace();
        }
        nextMediaPlayer.prepareAsync();
        Log.d("SoundService", "prepare  115");
    }

    public void stopSound() {
        mediaPlayer.stop();
    }
}
