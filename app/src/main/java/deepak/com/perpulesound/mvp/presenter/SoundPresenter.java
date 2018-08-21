package deepak.com.perpulesound.mvp.presenter;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import deepak.com.perpulesound.db.DatabaseService;
import deepak.com.perpulesound.model.Data;
import deepak.com.perpulesound.model.SoundApiResponse;
import deepak.com.perpulesound.mvp.view.ISoundView;
import deepak.com.perpulesound.network.SoundApiService;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SoundPresenter {

    private SoundApiService soundApiService;
    private ISoundView iSoundView;
    private DatabaseService databaseService;

    @Inject
    public SoundPresenter(SoundApiService soundApiService) {
        this.soundApiService = soundApiService;
        databaseService = DatabaseService.getInstance();
    }

    public void setiSoundView(ISoundView iSoundView) {
        this.iSoundView = iSoundView;
    }

    public void getSoundData() {
        iSoundView.showLoading();
        soundApiService.getSoundResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SoundApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(SoundApiResponse response) {
                        if (response != null && response.getData() != null
                                && response.getData().size() > 0) {
                            iSoundView.showSoundDataItems(response.getData());
                            saveResponseToDB(response.getData());
                            return;
                        }
                        iSoundView.showDataUnavailableView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iSoundView.showErrorView();
                    }

                    @Override
                    public void onComplete() {
                        iSoundView.hideLoading();
                    }
                });
    }

    private void saveResponseToDB(List<Data> data) {
        databaseService.writeSoundData(data);
    }

    public void fetchResponseFromDB() {
        iSoundView.showLoading();
        Observable.fromArray(databaseService.readSoundData())
                .subscribe(new Observer<List<Data>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Data> value) {
                        if (value != null && !value.isEmpty()) {
                            iSoundView.showSoundDataItems(value);
                            return;
                        }
                        iSoundView.showDataUnavailableView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iSoundView.showErrorView();
                    }

                    @Override
                    public void onComplete() {
                        iSoundView.hideLoading();
                    }
                });
    }
}
