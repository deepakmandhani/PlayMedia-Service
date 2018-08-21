package deepak.com.perpulesound.network;

import deepak.com.perpulesound.model.SoundApiResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface SoundApiService {

    @GET("mxcsl/")
    Observable<SoundApiResponse> getSoundResponse();
}
