package remcode.apps.notaza.presentation.category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import info.hoang8f.android.segmented.SegmentedGroup;
import remcode.apps.notaza.R;
import remcode.apps.notaza.presentation.PictureSelectionFragment;
import remcode.apps.notaza.unsplashapi.model.UnsplashPic;

public class NewCategoryActivity extends AppCompatActivity implements PictureSelectionFragment.FragmentCallback {

    public static final String EXTRA_BUNDLE = "my.apps.skillstracker.BUNDLE";
    public static final String EXTRA_NAME = "my.apps.skillstracker.NAME";
    public static final String EXTRA_DESCRIPTION = "my.apps.skillstracker.DESCRIPTION";
    public static final String EXTRA_UNSPLASHPICTURE = "my.apps.skillstracker.UNSPLASHPICTURE";
    public static final String EXTRA_TYPE = "my.apps.skillstracker.CATEGORYTYPE";
    private final String CLIENT_ID = "7b3b9ec9a8f3057b1831c2d14d6af52e18b6bd9ba2469eec612d75d1ac007676";

    private EditText mEditCategoryNameView, mEditCategoryDescriptionView;
    private Boolean pictureSelected = false;
    private Boolean isReadyToSave = false;
    private String categoryDescription;
    private String categoryName="";
    private Button continueButton;
    private Button saveButton;
    private Toolbar toolbar;
    private SegmentedGroup mSegmentedGroup;
    private UnsplashPic unsplashPicReceived;
    public PictureSelectionFragment pictureSelectionFragment;
    private PicDownloaderImp picDownloader;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        toolbar                      = findViewById(R.id.add_new_category_toolbar);
        saveButton                   = findViewById(R.id.savebutton_newcategory);
        continueButton               = findViewById(R.id.continue_button_new_category);
        mSegmentedGroup              = findViewById(R.id.categoryTypeSegmentedGroup);
        mEditCategoryNameView        = findViewById(R.id.edit_category_name);
        mEditCategoryDescriptionView = findViewById(R.id.edit_category_description);
        setSupportActionBar(toolbar);

        picDownloader = new PicDownloaderImp();

        pictureSelectionFragment = new PictureSelectionFragment();
        continueButton.setOnClickListener(view -> {
            // No radio buttons selected
            switch (mSegmentedGroup.getCheckedRadioButtonId()) {
                case -1:
                    Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.categoryTypeSelectionNeeded),
                            Toast.LENGTH_SHORT)
                            .show();
                    break;

                default:
                    if (!TextUtils.isEmpty(mEditCategoryNameView.getText())) {

                        categoryName = mEditCategoryNameView.getText().toString();
                        categoryDescription = mEditCategoryDescriptionView.getText().toString();
                        hideKeyboard(NewCategoryActivity.this);
                        continueButton.setVisibility(Button.GONE);

                        pictureSelected = true;
                        Bundle bundle = new Bundle();
                        bundle.putString("categoryName", categoryName);
                        pictureSelectionFragment.setArguments(bundle);

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction.add(R.id.fragment_container, pictureSelectionFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                break;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isReadyToSave){
            isReadyToSave = false;
            saveButton.setVisibility(View.GONE);
        }
        if (pictureSelected){
            pictureSelected = false;
            continueButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() { super.onResume(); }

    @Override
    public void onDataSent(UnsplashPic unsplashPic) {
        this.unsplashPicReceived = unsplashPic;
        saveButton.setVisibility(Button.VISIBLE);
        isReadyToSave = true;
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container))
                    .commit();
        }

        saveButton.setOnClickListener(v -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditCategoryNameView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {

                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_NAME, categoryName);
                bundle.putString(EXTRA_DESCRIPTION, categoryDescription);
                bundle.putString(EXTRA_UNSPLASHPICTURE, unsplashPicReceived.stringify());
                bundle.putInt(EXTRA_TYPE, getCategoryType());
                replyIntent.putExtra(EXTRA_BUNDLE, bundle);
                setResult(RESULT_OK, replyIntent);

                String downloadLink = unsplashPic.getLinks()
                        .getDownload_location() + "?client_id=" + CLIENT_ID;
                picDownloader.requestDownloadLink(downloadLink);
                // TODO continue with downloader class!!!
                // requestDownload the picture
                //picDownloader.requestDownload(unsplashPic);
            }
            finish();
        });
    }

    private Integer getCategoryType() {
        int focusedButton = mSegmentedGroup.getCheckedRadioButtonId();
        switch (focusedButton) {
            case R.id.notesBtnSegGroup:
                return 1;
            case R.id.skillsBtnSegGroup:
                return 2;
            default:
                return 0;
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        // Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        // If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}