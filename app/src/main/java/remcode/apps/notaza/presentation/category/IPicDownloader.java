package remcode.apps.notaza.presentation.category;

import remcode.apps.notaza.unsplashapi.model.UnsplashResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

interface IPicDownloader {
    @GET
    Call<UnsplashResponse> requestDownload(@Url String url);
}
