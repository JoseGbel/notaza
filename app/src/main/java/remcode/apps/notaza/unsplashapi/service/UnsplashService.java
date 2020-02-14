package remcode.apps.notaza.unsplashapi.service;

import remcode.apps.notaza.unsplashapi.model.UnsplashResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface UnsplashService {

    @GET("search/photos")
    Call<UnsplashResponse> getPictureList(@Query("query") String keyword,
                                          @Query("client_id") String id);

    @GET
    Call<UnsplashResponse> requestDownload(@Url String url);

}
