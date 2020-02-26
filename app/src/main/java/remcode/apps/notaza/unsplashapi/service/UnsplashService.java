package remcode.apps.notaza.unsplashapi.service;

import remcode.apps.notaza.unsplashapi.model.UnsplashResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface UnsplashService {

    String CLIENT_ID = "7b3b9ec9a8f3057b1831c2d14d6af52e18b6bd9ba2469eec612d75d1ac007676";
    String urlBase = "https://api.unsplash.com/";

    @GET("search/photos")
    Call<UnsplashResponse> getPictureList(@Query("query") String keyword,
                                          @Query("client_id") String id);

    @GET
    Call<UnsplashResponse> requestDownload(@Url String url);

}
