package my.apps.skillstracker.unsplashapi.service;

import my.apps.skillstracker.unsplashapi.model.PictureResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PictureService {

    @GET("search/photos")
    Call<PictureResponse> getPictureList(@Query("query") String keyword,
                                         @Query("client_id") String id);

}
