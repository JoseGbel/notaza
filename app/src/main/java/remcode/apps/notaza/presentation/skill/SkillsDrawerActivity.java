package remcode.apps.notaza.presentation.skill;

import android.app.SearchManager;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import remcode.apps.notaza.model.Category;
import remcode.apps.notaza.R;
import remcode.apps.notaza.model.Skill;
import remcode.apps.notaza.presentation.category.CategoryActivity;
import remcode.apps.notaza.presentation.adapters.RecyclerItemTouchHelper;
import remcode.apps.notaza.presentation.adapters.SkillListAdapter;
import remcode.apps.notaza.presentation.category.ChangePictureActivity;
import remcode.apps.notaza.presentation.category.EditCategoryActivity;
import remcode.apps.notaza.repositories.SkillRepository;
import remcode.apps.notaza.repositories.SkillViewModel;
import remcode.apps.notaza.repositories.SkillViewModelFactory;

public class SkillsDrawerActivity extends AppCompatActivity
        implements SkillListAdapter.SkillListAdapterListener,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    public static final int NEW_SKILL_ACTIVITY_REQUEST_CODE = 1;
    private static final int EDIT_SKILL_ACTIVITY_REQUEST_CODE = 2;
    private static final int EDIT_CATEGORY_ACTIVITY_REQUEST_CODE = 3;
    private static final int CHANGE_PICTURE_REQUEST_CODE = 4;
    public static final String EXTRA_BUNDLE_EDITED_CATEGORY = "remcode.apps.notaza.BUNDLE";
    private SkillListAdapter mRecyclerViewAdapter;
    private SkillViewModel mSkillViewModel;
    private CoordinatorLayout coordinatorLayout;
    static private Category currentCategory;
    private SearchView searchView;
    public static boolean isEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        coordinatorLayout = findViewById(R.id.coordinatorlayout_drawer);

        currentCategory = Category.createFromString(getIntent()
                .getExtras()
                .getString("Category"));

        Toolbar toolbar = findViewById(R.id.toolbar_drawer);
        setSupportActionBar(toolbar);

        // SetActivity title to the selected category and capitalise first character
        setTitle(currentCategory.getMName().substring(0,1).toUpperCase()
                + currentCategory.getMName().substring(1));

        RecyclerView mRecyclerView = findViewById(R.id.recyclerview_drawer);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerViewAdapter = new SkillListAdapter(this, this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        whiteNotificationBar(mRecyclerView);

        mSkillViewModel = ViewModelProviders.of(this,
                new SkillViewModelFactory(getApplication(), currentCategory.getMName()))
                .get(SkillViewModel.class);

        mSkillViewModel.getAllSkills(currentCategory.getMName())
                .observe(this, skills -> {
                    // Update the cached copy of the skills in the adapter.
                    mRecyclerViewAdapter.setSkills(skills);
                });

        setFAB();
    }

    private void setFAB() {
        FloatingActionButton mFab = findViewById(R.id.fab_drawer);
        mFab.setOnClickListener(view -> {
            // Get a reference of the current category
            Bundle received = getIntent().getExtras();
            Intent intent =
                    new Intent (SkillsDrawerActivity.this, NewSkillActivity.class);

            assert received != null;
            intent.putExtras(received);
            startActivityForResult(intent, NEW_SKILL_ACTIVITY_REQUEST_CODE);
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_SKILL_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = new Bundle(data.getBundleExtra(NewSkillActivity.EXTRA_BUNDLE));

                Skill skill = new Skill(
                        bundle.getString(NewSkillActivity.EXTRA_NAME),
                        bundle.getString(NewSkillActivity.EXTRA_DESCRIPTION),
                        bundle.getInt(NewSkillActivity.EXTRA_EXPERIENCE),
                        bundle.getString(NewSkillActivity.EXTRA_CATEGORY));

                mSkillViewModel.insert(skill);
            }
        }

        if (requestCode == CHANGE_PICTURE_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                String pictureStringified = data.getStringExtra("NewPictureObject");

                Intent backToCategoryActivity = new Intent(SkillsDrawerActivity.this,
                        CategoryActivity.class);

                backToCategoryActivity.putExtra(
                        ChangePictureActivity.EXTRA_PIC_STRING, pictureStringified);
                backToCategoryActivity.putExtra(
                        ChangePictureActivity.EXTRA_ID, currentCategory.getMId());
                // TODO think of a new way to avoid this static coupling. State-driven dev?
                CategoryActivity.editingPicture = true;

                startActivity(backToCategoryActivity);
            }
        }

        if (requestCode == EDIT_SKILL_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Bundle bundle = new Bundle(data.getBundleExtra(EditSkillActivity.EXTRA_BUNDLE));

                mSkillViewModel.update(new SkillRepository.MyTaskParams(
                        bundle.getInt(EditSkillActivity.EXTRA_ID),
                        bundle.getString(EditSkillActivity.EXTRA_NAME),
                        bundle.getString(EditSkillActivity.EXTRA_DESCRIPTION),
                        bundle.getInt(EditSkillActivity.EXTRA_EXPERIENCE)));
            }
        }

        if (requestCode == EDIT_CATEGORY_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Intent backToMainScreen = new Intent(this, CategoryActivity.class);
                Bundle bundleReceived = data.getBundleExtra(EditCategoryActivity.EXTRA_BUNDLE);
                backToMainScreen.putExtra(EXTRA_BUNDLE_EDITED_CATEGORY, bundleReceived);
                startActivity(backToMainScreen);
            }else{
                Toast.makeText(
                        getApplicationContext(),
                        R.string.empty_category_name,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof SkillListAdapter.SkillViewHolder) {
            // get the removed item nameTv to display it in snack bar
            String name = mRecyclerViewAdapter
                    .getSkills().get(viewHolder.getAdapterPosition()).getMName();

            // backup of removed skill for undo purpose
            final Skill deletedSkill = mRecyclerViewAdapter
                    .getSkills().get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the skill from recycler view
            mRecyclerViewAdapter.removeSkill(viewHolder.getAdapterPosition());

            // remove the skill from the database
            mSkillViewModel.delete(deletedSkill);

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + getString(R.string.removedFromList), Snackbar.LENGTH_LONG);
            snackbar.setAction(getString(R.string.undo), view -> {
                // undo is selected, restore the deleted skill
                mRecyclerViewAdapter.restoreSkill(deletedSkill, deletedIndex);
                // Insert the deletedSkill in the database again
                mSkillViewModel.insert(deletedSkill);
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drawer, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.actionsearch_main)
                .getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mRecyclerViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mRecyclerViewAdapter.getFilter().filter(query);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.actionsearch_main:
                return true;

            case (R.id.change_picture_menu_item):
                Intent changePicture = new Intent(SkillsDrawerActivity.this,
                        ChangePictureActivity.class);
                changePicture.putExtra("pictureToChange", currentCategory.getMPicture()
                        .stringify());
                startActivityForResult(changePicture, CHANGE_PICTURE_REQUEST_CODE);
                return true;

            case R.id.edit_category_menu_item:
                Intent intent = new Intent(
                        SkillsDrawerActivity.this, EditCategoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", currentCategory.getMId());
                bundle.putString("name", currentCategory.getMName());
                bundle.putString("description", currentCategory.getMDescription());
                //TODO must delete this check of null type once database is updated
                if(currentCategory.getMType()!=null)
                    bundle.putInt("categoryType", currentCategory.getMType());
                intent.putExtras(bundle);
                startActivityForResult(intent, EDIT_CATEGORY_ACTIVITY_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onSkillSelected(Skill skill) {

        Intent intent = new Intent(SkillsDrawerActivity.this, EditSkillActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", skill.getMId());
        bundle.putString("name", skill.getMName());
        bundle.putString("description", skill.getMDescription());
        bundle.putInt("experience", skill.getMExperience());
        bundle.putString("category", skill.getMCategory());
        intent.putExtras(bundle);
        startActivityForResult(intent, EDIT_SKILL_ACTIVITY_REQUEST_CODE);
    }
}