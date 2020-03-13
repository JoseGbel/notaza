package remcode.apps.notaza.presentation.category;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import remcode.apps.notaza.R;
import remcode.apps.notaza.presentation.adapters.PicSelectionRecyclerViewAdapter;
import remcode.apps.notaza.presentation.skill.SkillsDrawerActivity;
import remcode.apps.notaza.unsplashapi.model.UnsplashPic;
import remcode.apps.notaza.unsplashapi.model.UnsplashResponse;
import remcode.apps.notaza.unsplashapi.service.UnsplashService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePictureActivity extends AppCompatActivity implements
        PicSelectionRecyclerViewAdapter.RecyclerViewAdapterListener {

    public static final String EXTRA_PIC_STRING = "165156";
    public static final String EXTRA_ID = "1251151";
    public static final int UPDATE_PICTURE = 515;
    private static final String TAG = "unsplash";
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private PicSelectionRecyclerViewAdapter picSelectionRecyclerViewAdapter;
    private Button searchBtn;
    private EditText tryAgainEditText;

    // TODO think of a way to avoid this memory leak.
    private static View previousView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_picture);

        tryAgainEditText = findViewById(R.id.tryAgainEditText);
        recyclerView = findViewById(R.id.recyclerview);
        searchBtn = findViewById(R.id.searchBtnChangePicture);
        ImageView currentImageView = findViewById(R.id.current_picture);

        String myStringExtra = getIntent().getStringExtra("pictureToChange");
        if (myStringExtra != null && getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_PORTRAIT) {
            UnsplashPic currentPicture = UnsplashPic.createFromString(myStringExtra);
            Glide.with(this)
                    .load(currentPicture.getUrls().getRegular())
                    .into(currentImageView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        retrofit = new Retrofit.Builder()
                .baseUrl(UnsplashService.urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            layoutManager.setSpanCount(3);

        picSelectionRecyclerViewAdapter = new PicSelectionRecyclerViewAdapter(this,
                this);
        recyclerView.setAdapter(picSelectionRecyclerViewAdapter);

        searchBtn.setOnClickListener(view -> {
            clearData(); // Clear previous pictures from list
            getData(String.valueOf(tryAgainEditText.getText())); // Request pics from new keyword
            hideKeyboard(this);
        });
    }

    void clearData(){
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

                    TextView textViewFrame = findViewById(R.id.backgroundPictureTextViewFrame);
                    if (textViewFrame != null)
                        textViewFrame.setText(getString(R.string.select_a_picture));
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
        if (previousView != null) {
            ImageView previousImageView = previousView.findViewById(R.id.unsplash_imageview);
            previousImageView.setImageAlpha(255);
        }

        ImageView currentImageView = currentView.findViewById(R.id.unsplash_imageview);
        currentImageView.setImageAlpha(60);
        previousView = currentView;
        Snackbar snackbar = Snackbar
                .make(recyclerView, getString(R.string.selectThisPictureQuestion), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.YES), view -> {
            Intent backToCategoryActivity = new Intent(this,
                    SkillsDrawerActivity.class);
            backToCategoryActivity.putExtra("NewPictureObject", unsplashPic.stringify());
            setResult(RESULT_OK, backToCategoryActivity);
            finish();
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE);

        // Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();

        // If no view currently has focus, create a new one,
        // just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
