package deepak.com.perpulesound.di.component;

import javax.inject.Singleton;

import dagger.Component;
import deepak.com.perpulesound.activity.SoundActivity;
import deepak.com.perpulesound.di.module.AppModule;
import deepak.com.perpulesound.di.module.NetworkModule;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    void inject(SoundActivity soundActivity);
}
