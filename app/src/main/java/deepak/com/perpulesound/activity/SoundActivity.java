package deepak.com.perpulesound.activity;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import deepak.com.perpulesound.MyApplication;
import deepak.com.perpulesound.R;
import deepak.com.perpulesound.model.Data;
import deepak.com.perpulesound.mvp.presenter.SoundPresenter;
import deepak.com.perpulesound.mvp.view.ISoundView;

public class SoundActivity extends AppCompatActivity implements ISoundView {

    @Inject
    SoundPresenter soundPresenter;

    @BindView(R.id.continue_button)
    Button continueButton;

    @BindView(R.id.sound_data_view)
    LinearLayout soundItemView;

    @BindView(R.id.other_view)
    ImageView defaultView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Unbinder unbinder;
    private FragmentManager fragmentManager = getFragmentManager();
    private List<Data> dataList;
    private int soundIndex;
    private Intent soundIntent;
    private SoundService soundService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            soundService = ((SoundService.SoundBinder) iBinder).getSoundService();
            soundService.setDataList(dataList);
            launchNextSoundItem();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        unbinder = ButterKnife.bind(this);
        initDI();
        soundPresenter.setiSoundView(this);
        if (isNetworkConeected()) {
            soundPresenter.getSoundData();
        } else {
            soundPresenter.fetchResponseFromDB();
        }
    }

    private void initDI() {
        ((MyApplication) getApplication()).getAppComponent().inject(this);
    }

    private boolean isNetworkConeected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        stopService(soundIntent);
        soundService = null;
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
/*        if(fragmentManager.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }*/
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSoundDataItems(List<Data> dataList) {

        if (soundIntent == null) {
            soundIntent = new Intent(this, SoundService.class);
            bindService(soundIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            //startService(soundIntent);
        }
        continueButton.setEnabled(true);
        continueButton.setVisibility(View.VISIBLE);
        soundItemView.setVisibility(View.VISIBLE);
        defaultView.setVisibility(View.GONE);
        this.dataList = dataList;
    }

    @Override
    public void showErrorView() {
        continueButton.setVisibility(View.GONE);
        soundItemView.setVisibility(View.GONE);
        defaultView.setVisibility(View.VISIBLE);
        defaultView.setImageResource(R.drawable.error);
    }

    @Override
    public void showDataUnavailableView() {
        continueButton.setVisibility(View.GONE);
        soundItemView.setVisibility(View.GONE);
        defaultView.setVisibility(View.VISIBLE);
        defaultView.setImageResource(R.drawable.no_data);
    }

    @OnClick(R.id.continue_button)
    public void onContinueButtonClicked() {
        soundService.stopSound();
        if (soundIndex < dataList.size()) {
            launchNextSoundItem();
        } else {
            Toast.makeText(this, "No more Sound available, Play again", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void launchNextSoundItem() {
        android.app.Fragment fragment = SoundItemFragment.newInstance(dataList.get(soundIndex));
        fragmentManager.beginTransaction().replace(R.id.sound_data_view, fragment).commit();
        soundService.playSound(soundIndex++);
    }
}
