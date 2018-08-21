package deepak.com.perpulesound.db;

import java.util.ArrayList;
import java.util.List;

import deepak.com.perpulesound.model.Data;
import io.realm.Realm;
import io.realm.RealmResults;

public class DatabaseService {

    private static DatabaseService databaseService;
    private final Realm realm;

    private DatabaseService() {
        realm = Realm.getDefaultInstance();
    }

    public static DatabaseService getInstance() {
        if (databaseService == null) {
            databaseService = new DatabaseService();
        }
        return databaseService;
    }

    public void writeSoundData(List<Data> dataList) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(dataList);
        realm.commitTransaction();
    }

    public List<Data> readSoundData() {
        List<Data> dataList = new ArrayList<>();
        realm.beginTransaction();
        RealmResults<Data> realmResults = realm.where(Data.class).findAll();
        dataList.addAll(realm.copyFromRealm(realmResults));
        realm.commitTransaction();
        return dataList;
    }

    public void closeRealmDB() {
        realm.close();
    }
}
