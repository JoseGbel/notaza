package remcode.apps.notaza.presentation.skill;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import remcode.apps.notaza.model.Category;
import remcode.apps.notaza.R;
import remcode.apps.notaza.model.Skill;
import remcode.apps.notaza.presentation.category.CategoryActivity;
import remcode.apps.notaza.presentation.adapters.RecyclerItemTouchHelper;
import remcode.apps.notaza.presentation.adapters.SkillListAdapter;
import remcode.apps.notaza.presentation.category.EditCategoryActivity;
import remcode.apps.notaza.repositories.CategoryViewModel;
import remcode.apps.notaza.repositories.SkillRepository;
import remcode.apps.notaza.repositories.SkillViewModel;
import remcode.apps.notaza.repositories.SkillViewModelFactory;

public class SkillsDrawerActivity extends AppCompatActivity
        implements SkillListAdapter.SkillListAdapterListener,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    public static final int NEW_SKILL_ACTIVITY_REQUEST_CODE = 1;
    private static final int EDIT_SKILL_ACTIVITY_REQUEST_CODE = 2;
    private static final int EDIT_CATEGORY_ACTIVITY_REQUEST_CODE = 3;
    public static final String EXTRA_BUNDLE_EDITED_CATEGORY = "remcode.apps.notaza.BUNDLE";

    private SkillListAdapter mRecyclerViewAdapter;
    private SkillViewModel mSkillViewModel;
    private CoordinatorLayout coordinatorLayout;
    static private Category currentCategory;
    private SearchView searchView;
    public static boolean isEditing;
    private CategoryViewModel mCategoryViewModel;

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
                .observe(this, new Observer<List<Skill>>() {
            @Override
            public void onChanged(@Nullable final List<Skill> skills) {
                // Update the cached copy of the skills in the adapter.
                mRecyclerViewAdapter.setSkills(skills);
            }
        });

        setFAB();
    }

    private void setFAB() {
        FloatingActionButton mFab = findViewById(R.id.fab_drawer);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get a reference of the current category
                Bundle received = getIntent().getExtras();
                Intent intent =
                        new Intent (SkillsDrawerActivity.this, NewSkillActivity.class);

                assert received != null;
                intent.putExtras(received);
                startActivityForResult(intent, NEW_SKILL_ACTIVITY_REQUEST_CODE);
            }
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
            snackbar.setAction(getString(R.string.undo), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted skill
                    mRecyclerViewAdapter.restoreSkill(deletedSkill, deletedIndex);
                    // Insert the deletedSkill in the database again
                    mSkillViewModel.insert(deletedSkill);
                }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.actionsearch_main:
                return true;
            case R.id.delete_category_menu_item:
                Toast.makeText(SkillsDrawerActivity.this,
                        "Delete Category", Toast.LENGTH_SHORT)
                .show();
                return true;
            case R.id.edit_category_menu_item:
                Intent intent = new Intent(SkillsDrawerActivity.this, EditCategoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", currentCategory.getMId());
                bundle.putString("name", currentCategory.getMName());
                bundle.putString("description", currentCategory.getMDescription());
                //TODO must delete this check of null type once database is updated
                if(currentCategory.getMType()!=null)
                    bundle.putInt("categoryType", currentCategory.getMType());
                intent.putExtras(bundle);
//                editingCategory = true;
                startActivityForResult(intent, EDIT_CATEGORY_ACTIVITY_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //noinspection SimplifiableIfStatement
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
//        editingCategory = true;
        startActivityForResult(intent, EDIT_SKILL_ACTIVITY_REQUEST_CODE);
    }
}