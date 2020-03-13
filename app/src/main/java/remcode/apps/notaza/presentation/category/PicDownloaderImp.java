package remcode.apps.notaza.presentation.category;

import android.util.Log;

import java.util.logging.Level;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import remcode.apps.notaza.unsplashapi.model.UnsplashResponse;
import remcode.apps.notaza.unsplashapi.service.UnsplashService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PicDownloaderImp {
    private Retrofit retrofit;
    private static final String TAG = "downloadRequest";

    public PicDownloaderImp(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message ->
                Log.d(TAG, message));
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        String urlBase = "https://api.unsplash.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(UnsplashService.urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                 .client(client)
                .build();
    }


    public void requestDownloadLink(String url) {
        UnsplashService downloader = retrofit.create(UnsplashService.class);
        Call<UnsplashResponse> networkRequest = downloader.requestDownload(url);

        networkRequest.enqueue(new Callback<UnsplashResponse>() {


            @Override
            public void onResponse(Call<UnsplashResponse> call,
                                   Response<UnsplashResponse> response){
                if (response.isSuccessful()) {

                    String downloadLink = response.body().getUrl();
                    fireDownload(downloadLink);
                } else {
                    Log.e(TAG, " onResponse " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UnsplashResponse> call, Throwable t) {
                Log.e(TAG, " onFailure " + t.getMessage());
            }
        });
    }

    public void fireDownload(String url) {
        IPicDownloader downloader = retrofit.create(IPicDownloader.class);
        Call<UnsplashResponse> networkRequest = downloader.requestDownload(url);

        networkRequest.enqueue(new Callback<UnsplashResponse>() {
            private static final String TAG2 = "fireDownload";

            @Override
            public void onResponse(Call<UnsplashResponse> call, Response<UnsplashResponse> response) {
                if (response.isSuccessful()) {

                    String downloadLink = response.body().getUrl();
                    fireDownload(downloadLink);
                } else {
                    Log.e(TAG2, " onResponse " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UnsplashResponse> call, Throwable t) {
                Log.e(TAG2, " onFailure " + t.getMessage());
            }
        });
    }

}
