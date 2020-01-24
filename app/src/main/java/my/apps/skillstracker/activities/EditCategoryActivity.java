package my.apps.skillstracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import info.hoang8f.android.segmented.SegmentedGroup;
import my.apps.skillstracker.R;

public class EditCategoryActivity extends AppCompatActivity {

    public static final String EXTRA_BUNDLE = "my.apps.skillstracker.BUNDLE";
    public static final String EXTRA_NAME = "my.apps.skillstracker.NAME";
    public static final String EXTRA_DESCRIPTION = "my.apps.skillstracker.DESCRIPTION";
    public static final String EXTRA_CATEGORYTYPE = "my.apps.skillstracker.EXPERIENCE";
    public static final String EXTRA_ID = "my.apps.skillstracker.ID";
    private EditText mEditCategoryNameView, mEditCategoryDescriptionView;
    private int id;
    private SegmentedGroup mSegmentedGroup;
    private Button notesBtn, skillsBtn, placesBtn, todoBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        mEditCategoryNameView = findViewById(R.id.edit_category_name);
        mEditCategoryDescriptionView = findViewById(R.id.edit_category_description);
        mSegmentedGroup = findViewById(R.id.categoryTypeSegmentedGroup);
        notesBtn = findViewById(R.id.notesBtnSegGroup);
        skillsBtn = findViewById(R.id.skillsBtnSegGroup);
        placesBtn = findViewById(R.id.placesBtnSegGroup);
        todoBtn = findViewById(R.id.toDoBtnSegGroup);

        final Button button = findViewById(R.id.continue_button_new_category);

        Toolbar toolbar = findViewById(R.id.add_new_category_toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        setTitle(getString(R.string.edit)
                + " " + bundle.getString("name") + " "
                + getString(R.string.category));
        mEditCategoryNameView.setText(bundle.getString("name"));
        mEditCategoryDescriptionView.setText(bundle.getString("description"));
        id = bundle.getInt("id");

        switch (bundle.getInt("categoryType")) {
            case 1:
                notesBtn.performClick();
                break;
            case 2:
                skillsBtn.performClick();
                break;
            case 3:
                placesBtn.performClick();
                break;
            case 4:
                todoBtn.performClick();
                break;
            default:
                break;
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // No radio Buttons selected
                if (mSegmentedGroup.getCheckedRadioButtonId() == -1)
                    Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.categoryTypeSelectionNeeded),
                            Toast.LENGTH_SHORT)
                            .show();

                else { //TODO delete this IF once Places are implemented
                    if (mSegmentedGroup.getCheckedRadioButtonId() == R.id.placesBtnSegGroup){
                        Toast.makeText(
                                getApplicationContext(),
                                "List of places are not available yet. " +
                                        "You can add a list of places as a Note list",
                                Toast.LENGTH_SHORT)
                                .show();
                    }else {
                        Intent replyIntent = new Intent();
                        if (TextUtils.isEmpty(mEditCategoryNameView.getText()))
                            setResult(RESULT_CANCELED, replyIntent);

                        else {
                            String categoryName = mEditCategoryNameView.getText().toString();
                            String categoryDescription =
                                    mEditCategoryDescriptionView.getText().toString();

                            Bundle bundle = new Bundle();
                            bundle.putString(EXTRA_NAME, categoryName);
                            bundle.putString(EXTRA_DESCRIPTION, categoryDescription);
                            bundle.putInt(EXTRA_CATEGORYTYPE, getCategoryType());
                            bundle.putInt(EXTRA_ID, id);
                            replyIntent.putExtra(EXTRA_BUNDLE, bundle);
                            setResult(RESULT_OK, replyIntent);
                            MainActivity.editingCategory = true;
                        }
                        finish();
                    }
                }
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
            case R.id.placesBtnSegGroup:
                return 3;
            case R.id.toDoBtnSegGroup:
                return 4;
            default:
                return 0;
        }
    }
}
