package my.apps.skillstracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import info.hoang8f.android.segmented.SegmentedGroup;
import my.apps.skillstracker.R;
import my.apps.skillstracker.unsplashapi.model.UnsplashPic;

public class NewCategoryActivity extends AppCompatActivity implements PictureSelectionFragment.FragmentCallback{

    public static final String EXTRA_BUNDLE = "my.apps.skillstracker.BUNDLE";
    public static final String EXTRA_NAME = "my.apps.skillstracker.NAME";
    public static final String EXTRA_DESCRIPTION = "my.apps.skillstracker.DESCRIPTION";
    public static final String EXTRA_UNSPLASHPICTURE = "my.apps.skillstracker.UNSPLASHPICTURE";
    public static final String EXTRA_TYPE = "my.apps.skillstracker.CATEGORYTYPE";

    private EditText mEditCategoryNameView, mEditCategoryDescriptionView;
    private Boolean pictureSelected = false;
    private Boolean isReadyToSave = false;
    private String categoryName="";
    private String categoryDescription;
    private Button continueButton;
    private Button saveButton;
    private SegmentedGroup mSegmentedGroup;
    private UnsplashPic unsplashPicReceived;
    public PictureSelectionFragment pictureSelectionFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        mEditCategoryNameView = findViewById(R.id.edit_category_name);
        mEditCategoryDescriptionView = findViewById(R.id.edit_category_description);
        mSegmentedGroup = findViewById(R.id.categoryTypeSegmentedGroup);
        continueButton = findViewById(R.id.continue_button_new_category);
        saveButton = findViewById(R.id.savebutton_newcategory);
        Toolbar toolbar = findViewById(R.id.add_new_category_toolbar);
        setSupportActionBar(toolbar);

        pictureSelectionFragment
                = new PictureSelectionFragment();

        continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // No radio buttons selected
                switch (mSegmentedGroup.getCheckedRadioButtonId()) {
                    case -1:
                        Toast.makeText(
                                getApplicationContext(),
                                getString(R.string.categoryTypeSelectionNeeded),
                                Toast.LENGTH_SHORT)
                                .show();
                        break;
                    //TODO Places todo
//                    case R.id.placesBtnSegGroup:
//                        Toast.makeText(
//                                getApplicationContext(),
//                                "List of places are not available yet. " +
//                                        "You can add a list of places as a Note list",
//                                Toast.LENGTH_SHORT)
//                                .show();
//                        break;
//                    case R.id.toDoBtnSegGroup:
//                        break;
                    default:
                        if (!TextUtils.isEmpty(mEditCategoryNameView.getText())) {

                            categoryName = mEditCategoryNameView.getText().toString();
                            categoryDescription = mEditCategoryDescriptionView.getText().toString();

                            continueButton.setVisibility(Button.GONE);

                            pictureSelected = true;
                            Bundle bundle = new Bundle();
                            bundle.putString("categoryName", categoryName);
                            pictureSelectionFragment.setArguments(bundle);

                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            //transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                            transaction.add(R.id.fragment_container, pictureSelectionFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    break;
                }
//            }
//                if (mSegmentedGroup.getCheckedRadioButtonId() == -1)
//                    Toast.makeText(
//                            getApplicationContext(),
//                            getString(R.string.categoryTypeSelectionNeeded),
//                            Toast.LENGTH_SHORT)
//                            .show();
//
//                else {
//                    //TODO delete this IF once implemented Places lists
//                    if (mSegmentedGroup.getCheckedRadioButtonId() == R.id.placesBtnSegGroup){
//                        Toast.makeText(
//                                getApplicationContext(),
//                                "List of places are not available yet. " +
//                                        "You can add a list of places as a Note list",
//                                Toast.LENGTH_SHORT)
//                                .show();
//                    }else {
//                        if (!TextUtils.isEmpty(mEditCategoryNameView.getText())) {
//
//                            categoryName = mEditCategoryNameView.getText().toString();
//                            categoryDescription = mEditCategoryDescriptionView.getText().toString();
//
//                            continueButton.setVisibility(Button.GONE);
//
//                            Bundle bundle = new Bundle();
//                            bundle.putString("categoryName", categoryName);
//                            pictureSelectionFragment.setArguments(bundle);
//
//                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                            //transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
//                            transaction.add(R.id.fragment_container, pictureSelectionFragment)
//                                    .addToBackStack(null)
//                                    .commit();
//                        }
//                    }
//                }
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                }
                finish();
            }
        });
    }

    private Integer getCategoryType() {
        Integer focusedButton = mSegmentedGroup.getCheckedRadioButtonId();
        switch (focusedButton) {
            case R.id.notesBtnSegGroup:
                return 1;
            case R.id.skillsBtnSegGroup:
                return 2;
            //TODO Places todo
//            case R.id.placesBtnSegGroup:
//                return 3;
//            case R.id.toDoBtnSegGroup:
//                return 4;
            default:
                return 0;
        }
    }
}