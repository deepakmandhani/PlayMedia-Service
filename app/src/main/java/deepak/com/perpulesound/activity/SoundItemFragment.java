package deepak.com.perpulesound.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import deepak.com.perpulesound.R;
import deepak.com.perpulesound.model.Data;

public class SoundItemFragment extends Fragment {

    private static final String SOUND_LIST = "SOUND_LIST";
    private Data data;

    @BindView(R.id.sound_title_text)
    TextView soundTitleText;

    private Unbinder unbinder;

    public SoundItemFragment() {
    }

    public static SoundItemFragment newInstance(Data data) {
        SoundItemFragment fragment = new SoundItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(SOUND_LIST, Parcels.wrap(data));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = Parcels.unwrap(getArguments().getParcelable(SOUND_LIST));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sound_item, container, false);
        unbinder = ButterKnife.bind(this, view);
        soundTitleText.setText(data.getDesc());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
       /* to play media in Fragment
       mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();
        mediaPlayer.setAudioAttributes(audioAttributes);
        try {
            mediaPlayer.setDataSource(data.getAudio());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

/*    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }*/
}
