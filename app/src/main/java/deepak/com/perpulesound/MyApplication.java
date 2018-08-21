package deepak.com.perpulesound;

import android.app.Application;

import deepak.com.perpulesound.di.component.AppComponent;
import deepak.com.perpulesound.di.component.DaggerAppComponent;
import deepak.com.perpulesound.di.module.AppModule;
import deepak.com.perpulesound.di.module.NetworkModule;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    private static final String DB_NAME = "SoundData.Realm";
    private static final int REALM_SCHEMA = 1;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();
        initializeRealmDB();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private void initializeRealmDB() {
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .name(DB_NAME)
                .schemaVersion(REALM_SCHEMA)
                .deleteRealmIfMigrationNeeded()
                .build());
    }
}
