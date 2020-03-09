package remcode.apps.notaza.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.util.ArrayList;
import remcode.apps.notaza.R;
import remcode.apps.notaza.presentation.adapters.PicSelectionRecyclerViewAdapter;
import remcode.apps.notaza.unsplashapi.model.UnsplashResponse;
import remcode.apps.notaza.unsplashapi.model.UnsplashPic;
import remcode.apps.notaza.unsplashapi.service.UnsplashService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PictureSelectionFragment extends Fragment
        implements PicSelectionRecyclerViewAdapter.RecyclerViewAdapterListener {

    private static final String TAG = "unsplash";
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private PicSelectionRecyclerViewAdapter picSelectionRecyclerViewAdapter;
    private FragmentCallback fragmentCallback; //Data passer to the activity
    private Button tryAgainBtn;
    private EditText tryAgainEditText;
    private String keyword;

    // TODO code smell
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picture_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getActivity().findViewById(R.id.recyclerview);
        tryAgainBtn = getActivity().findViewById(R.id.searchBtnChangePicture);
        tryAgainEditText = getActivity().findViewById(R.id.tryAgainEditText);
    }

    @Override
    public void onStart() {
        super.onStart();

        retrofit = new Retrofit.Builder()
                .baseUrl(UnsplashService.urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        // change number of columns in the layout if in landscape mode
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            layoutManager.setSpanCount(3);

        picSelectionRecyclerViewAdapter = new PicSelectionRecyclerViewAdapter(getContext(),
                this);
        recyclerView.setAdapter(picSelectionRecyclerViewAdapter);

        getData(keyword);

        tryAgainBtn.setOnClickListener(view -> {

            // Clear previous pictures from list
            clearData();

            // Request pics from new keyword
            getData(String.valueOf(tryAgainEditText.getText()));

            hideKeyboard(getActivity());
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        keyword= getArguments().getString("categoryName");
    }

    private void clearData(){
        picSelectionRecyclerViewAdapter.deletePictures();
    }

    private void getData(String keyword) {
        UnsplashService service = retrofit.create(UnsplashService.class);
        Call<UnsplashResponse> pictureResponseCall = service.getPictureList(
                keyword, UnsplashService.CLIENT_ID);

        pictureResponseCall.enqueue(new Callback<UnsplashResponse>() {
            @Override
            public void onResponse(Call<UnsplashResponse> call,
                                   Response<UnsplashResponse> response) {

                if (response.isSuccessful()){

                    UnsplashResponse pictureResponse = response.body();
                    ArrayList<UnsplashPic> pictureList = pictureResponse.getResults();

                    picSelectionRecyclerViewAdapter.addPictures(pictureList);

                }else{
                    Log.e (TAG, " onResponse " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UnsplashResponse> call, Throwable t) {
                Log.e (TAG, " onFailure " + t.getMessage());
            }
        });
    }

    @Override
    public void onPictureSelected(final UnsplashPic unsplashPic,
                                  View currentView) {

        // If not null there is a picture selected; set its alpha to max
        if (previousView != null) {
            ImageView previousImageView = previousView.findViewById(R.id.unsplash_imageview);
            previousImageView.setImageAlpha(255);
        }

        // Change alpha to 60 to show a selected item effect
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

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE);

        // Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();

        // If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
