package deepak.com.perpulesound.di.module;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import deepak.com.perpulesound.network.SoundApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
@Module
public class NetworkModule {

    @Singleton
    @Provides
    String provideBaseUrl() {
        return "https://api.myjson.com/bins/";
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    SoundApiService provideSoundApiService(Retrofit retrofit) {
        return retrofit.create(SoundApiService.class);
    }


}
