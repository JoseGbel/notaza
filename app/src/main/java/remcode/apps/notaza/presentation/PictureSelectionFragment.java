package remcode.apps.notaza.presentation;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import remcode.apps.notaza.R;
import remcode.apps.notaza.presentation.adapters.PicSelectionRecyclerViewAdapter;
import remcode.apps.notaza.unsplashapi.model.PictureResponse;
import remcode.apps.notaza.unsplashapi.model.UnsplashPic;
import remcode.apps.notaza.unsplashapi.service.PictureService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PictureSelectionFragment extends Fragment
        implements PicSelectionRecyclerViewAdapter.RecyclerViewAdapterListener {

    private static final String TAG = "unsplash";
    private final String CLIENT_ID = "7b3b9ec9a8f3057b1831c2d14d6af52e18b6bd9ba2469eec612d75d1ac007676";
    private final String urlBase = "https://api.unsplash.com/";
    private Retrofit retrofit;

    private RecyclerView recyclerView;
    private PicSelectionRecyclerViewAdapter picSelectionRecyclerViewAdapter;
    private FragmentCallback fragmentCallback; //Data passer to the activity
    private String keyword;
    private static View previousView;

    public PictureSelectionFragment(){

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentCallback = (FragmentCallback) context;
        previousView = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picture_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getActivity().findViewById(R.id.recyclerview);
    }

    @Override
    public void onStart() {
        super.onStart();

        retrofit = new Retrofit.Builder()
                .baseUrl(urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        picSelectionRecyclerViewAdapter = new PicSelectionRecyclerViewAdapter(getContext(), this);
        recyclerView.setAdapter(picSelectionRecyclerViewAdapter);

        getData(keyword);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        keyword= getArguments().getString("categoryName");
    }

    private void getData(String keyword) {
        PictureService service = retrofit.create(PictureService.class);
        Call<PictureResponse> pictureResponseCall = service.getPictureList(keyword, CLIENT_ID);

        pictureResponseCall.enqueue(new Callback<PictureResponse>() {
            @Override
            public void onResponse(Call<PictureResponse> call, Response<PictureResponse> response) {
                if (response.isSuccessful()){

                    PictureResponse pictureResponse = response.body();
                    ArrayList<UnsplashPic> pictureList = pictureResponse.getResults();

                    picSelectionRecyclerViewAdapter.addPictures(pictureList);

                }else{
                    Log.e (TAG, " onResponse " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PictureResponse> call, Throwable t) {
                Log.e (TAG, " onFailure " + t.getMessage());
            }
        });
    }

    @Override
    public void onPictureSelected(final UnsplashPic unsplashPic,
                                  View currentView) {

        if (previousView != null) {
            ImageView previousImageView = previousView.findViewById(R.id.unsplash_imageview);
            previousImageView.setImageAlpha(255);
        }

        // TODO SOME ANIMATIONS
        ImageView currentImageView = currentView.findViewById(R.id.unsplash_imageview);
        currentImageView.setImageAlpha(60);
        previousView = currentView;
        Snackbar snackbar = Snackbar
                .make(recyclerView, getString(R.string.selectThisPictureQuestion), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.YES), view -> fragmentCallback.onDataSent(unsplashPic));
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    public interface FragmentCallback {
        void onDataSent (UnsplashPic unsplashPic);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i (TAG, "Destroying fragment");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i (TAG, "Detaching fragment");
    }
}
