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

public class EditSkillActivity extends AppCompatActivity {

    public static final String EXTRA_BUNDLE = "my.apps.skillstracker.BUNDLE";
    public static final String EXTRA_NAME = "my.apps.skillstracker.NAME";
    public static final String EXTRA_DESCRIPTION = "my.apps.skillstracker.DESCRIPTION";
    public static final String EXTRA_CATEGORY = "my.apps.skillstracker.CATEGORY";
    public static final String EXTRA_EXPERIENCE = "my.apps.skillstracker.EXPERIENCE";
    public static final String EXTRA_ID = "my.apps.skillstracker.ID";
    private EditText mEditSkillNameView, mEditSkillDescriptionView;
    private String category;
    private int id, experience;
    private SegmentedGroup mSegmentedGroup;
    private Button noviceBtn, beginnerBtn, competentBtn, proficientBtn, expertBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_skill);

        mEditSkillNameView = findViewById(R.id.edit_skill_name);
        mEditSkillDescriptionView = findViewById(R.id.edit_skill_description);
        mSegmentedGroup = findViewById(R.id.experienceSegmentedGroup);
        noviceBtn = findViewById(R.id.noviceBtn);
        beginnerBtn = findViewById(R.id.beginnerBtn);
        competentBtn = findViewById(R.id.competentBtn);
        proficientBtn = findViewById(R.id.proficientBtn);
        expertBtn = findViewById(R.id.expertBtn);

        final Button button = findViewById(R.id.button_save);

        Toolbar toolbar = findViewById(R.id.add_new_skill_toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        mEditSkillNameView.setText(bundle.getString("name"));
        mEditSkillDescriptionView.setText(bundle.getString("description"));
        category = bundle.getString("category");
        id = bundle.getInt("id");

        setTitle(bundle.getString("name"));

        switch (bundle.getInt("experience")) {
            case 1:
                noviceBtn.performClick();
                break;
            case 2:
                beginnerBtn.performClick();
                break;
            case 3:
                competentBtn.performClick();
                break;
            case 4:
                proficientBtn.performClick();
                break;
            case 5:
                expertBtn.performClick();
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
                            getString(R.string.selectExperiencePlease),
                            Toast.LENGTH_SHORT)
                            .show();

                else {
                    Intent replyIntent = new Intent();
                    if (TextUtils.isEmpty(mEditSkillNameView.getText()))
                        setResult(RESULT_CANCELED, replyIntent);

                    else {
                        String skillName = mEditSkillNameView.getText().toString();
                        String skillDescription = mEditSkillDescriptionView.getText().toString();

                        Bundle bundle = new Bundle();
                        bundle.putString(EXTRA_NAME, skillName);
                        bundle.putString(EXTRA_DESCRIPTION, skillDescription);
                        bundle.putString(EXTRA_CATEGORY, category);
                        bundle.putInt(EXTRA_EXPERIENCE, getExperienceLevel());
                        bundle.putInt(EXTRA_ID, id);
                        replyIntent.putExtra(EXTRA_BUNDLE, bundle);
                        setResult(RESULT_OK, replyIntent);
                        DrawerActivity.isEditing = false;
                    }
                    finish();
                }
            }
        });
    }

    private Integer getExperienceLevel() {
        Integer focusedButton = mSegmentedGroup.getCheckedRadioButtonId();
        switch (focusedButton) {
            case R.id.noviceBtn:
                return 1;
            case R.id.beginnerBtn:
                return 2;
            case R.id.competentBtn:
                return 3;
            case R.id.proficientBtn:
                return 4;
            case R.id.expertBtn:
                return 5;
            default:
                return 0;
        }
    }
}
