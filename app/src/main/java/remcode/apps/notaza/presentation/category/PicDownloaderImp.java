package remcode.apps.notaza.presentation.category;

import android.util.Log;

import java.util.ArrayList;

import remcode.apps.notaza.unsplashapi.model.PictureResponse;
import remcode.apps.notaza.unsplashapi.model.UnsplashPic;
import remcode.apps.notaza.unsplashapi.service.PictureService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PicDownloaderImp implements IPicDownloader {
    private final String CLIENT_ID = "7b3b9ec9a8f3057b1831c2d14d6af52e18b6bd9ba2469eec612d75d1ac007676";
    private final String urlBase = "https://api.unsplash.com/";


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(urlBase)
                .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    public void download(UnsplashPic unsplashPic) {

    }

//    @Override
//    public void download(UnsplashPic unsplashPic) {
//        PictureService service = retrofit.create(PictureService.class);
////        Call<PictureResponse> pictureResponseCall = service.getPictureList(keyword, CLIENT_ID);
//
//        pictureResponseCall.enqueue(new Callback<PictureResponse>() {
//            @Override
//            public void onResponse(Call<PictureResponse> call, Response<PictureResponse> response) {
//                if (response.isSuccessful()){
//
//                    PictureResponse pictureResponse = response.body();
//                    ArrayList<UnsplashPic> pictureList = pictureResponse.getResults();
//
//                    picSelectionRecyclerViewAdapter.addPictures(pictureList);
//
//                }else{
//                    Log.e (TAG, " onResponse " + response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PictureResponse> call, Throwable t) {
//                Log.e (TAG, " onFailure " + t.getMessage());
//            }
//        });
//    }
}
