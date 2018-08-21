package deepak.com.perpulesound.mvp.view;

import java.util.List;

import deepak.com.perpulesound.model.Data;

public interface ISoundView {

    void showLoading();
    void hideLoading();
    void showSoundDataItems(List<Data> dataList);
    void showErrorView();
    void showDataUnavailableView();
}
