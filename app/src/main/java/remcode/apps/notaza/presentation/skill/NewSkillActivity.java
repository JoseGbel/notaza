package remcode.apps.notaza.presentation.skill;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import info.hoang8f.android.segmented.SegmentedGroup;
import remcode.apps.notaza.model.Category;
import remcode.apps.notaza.R;

public class NewSkillActivity extends AppCompatActivity {

    public static final String EXTRA_BUNDLE = "remcode.apps.notaza.BUNDLE";
    public static final String EXTRA_NAME = "remcode.apps.notaza.NAME";
    public static final String EXTRA_DESCRIPTION = "remcode.apps.notaza.DESCRIPTION";
    public static final String EXTRA_CATEGORY = "remcode.apps.notaza.CATEGORY";
    public static final String EXTRA_EXPERIENCE = "remcode.apps.notaza.EXPERIENCE";
    private EditText mEditSkillNameView, mEditSkillDescriptionView;
    private SegmentedGroup mSegmentedGroup;
    private Category currentCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_skill);

        Button noviceBtn, beginnerBtn, competentBtn, proficientBtn, expertBtn;
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

        currentCategory = Category.createFromString(
                getIntent().getExtras().getString("Category"));
        switch (currentCategory.getMType()){
            case 1: //CATEGORY TYPE: NOTES
                mSegmentedGroup.setVisibility(View.GONE);
            break;
            case 2: //CATEGORY TYPE: SKILLS
                mSegmentedGroup.setVisibility(View.VISIBLE);
                break;
                //TODO Places todo
//            case 3: //CATEGORY TYPE: PLACES
//                mSegmentedGroup.setVisibility(View.GONE);
//                break;
//            case 4: //CATEGORY TYPE: TO-DO LIST
//                mSegmentedGroup.setVisibility(View.GONE);
//                break;
            default:
                mSegmentedGroup.setVisibility(View.GONE);
                break;
        }
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mSegmentedGroup.getVisibility() == View.VISIBLE){
                    // No radio Buttons selected
                    if (mSegmentedGroup.getCheckedRadioButtonId() == -1)
                        Toast.makeText(
                                getApplicationContext(),
                                getString(R.string.selectExperiencePlease),
                                Toast.LENGTH_SHORT)
                                .show();
                }
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditSkillNameView.getText()))
                    setResult(RESULT_CANCELED, replyIntent);

                else {
                    String skillName = mEditSkillNameView.getText().toString();
                    String skillDescription = mEditSkillDescriptionView.getText().toString();

                    Bundle bundle = new Bundle();
                    bundle.putString(EXTRA_NAME, skillName);
                    bundle.putString(EXTRA_DESCRIPTION, skillDescription);
                    bundle.putString(EXTRA_CATEGORY, currentCategory.getMName());
                    bundle.putInt(EXTRA_EXPERIENCE, getExperienceLevel());
                    replyIntent.putExtra(EXTRA_BUNDLE, bundle);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
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
