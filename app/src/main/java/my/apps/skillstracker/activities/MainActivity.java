package my.apps.skillstracker.activities;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.util.List;

import my.apps.skillstracker.Category;
import my.apps.skillstracker.R;
import my.apps.skillstracker.repositories.CategoryRepository;
import my.apps.skillstracker.repositories.CategoryViewModel;
import my.apps.skillstracker.unsplashapi.model.UnsplashPic;

//TODO clean up the code to make it more readable
//TODO create an offline mode.
public class MainActivity extends AppCompatActivity
        implements CategoryListAdapter.CategoryListAdapterListener,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    public static final int NEW_SKILL_ACTIVITY_REQUEST_CODE = 1;
    private static final int EDIT_CATEGORY_ACTIVITY_REQUEST_CODE = 3;
    public static boolean editingCategory;

    private CategoryListAdapter mRecyclerViewAdapter;
    private CategoryViewModel mCategoryViewModel;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinatorlayout_main);

        RecyclerView mRecyclerView = findViewById(R.id.recyclerview_main);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerViewAdapter = new CategoryListAdapter(this, this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        whiteNotificationBar(mRecyclerView);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        mCategoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        mCategoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable final List<Category> categories) {
                // Update the cached copy of the skills in the adapter.
                mRecyclerViewAdapter.setCategories(categories);
            }
        });

        setFAB();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_SKILL_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            Bundle bundle = new Bundle(data.getBundleExtra(NewCategoryActivity.EXTRA_BUNDLE));

            Category category;
            switch(bundle.getInt(NewCategoryActivity.EXTRA_TYPE)) {
                //1 NOTES .. 2 SKILLS .. 3 PLACES .. 4 TO-DO
                case 1:
                    category = new Category(
                            bundle.getString(NewCategoryActivity.EXTRA_NAME),
                            bundle.getString(NewCategoryActivity.EXTRA_DESCRIPTION),
                            UnsplashPic.createFromString(
                                    bundle.getString(NewCategoryActivity.EXTRA_UNSPLASHPICTURE)),
                            1);
                    mCategoryViewModel.insert(category);
                    break;
                case 2:
                    category = new Category(
                            bundle.getString(NewCategoryActivity.EXTRA_NAME),
                            bundle.getString(NewCategoryActivity.EXTRA_DESCRIPTION),
                            UnsplashPic.createFromString(
                                    bundle.getString(NewCategoryActivity.EXTRA_UNSPLASHPICTURE)),
                            2);
                    mCategoryViewModel.insert(category);
                    break;
                case 3:
                    category = new Category(
                            bundle.getString(NewCategoryActivity.EXTRA_NAME),
                            bundle.getString(NewCategoryActivity.EXTRA_DESCRIPTION),
                            UnsplashPic.createFromString(
                                    bundle.getString(NewCategoryActivity.EXTRA_UNSPLASHPICTURE)),
                            3);
                    mCategoryViewModel.insert(category);
                    break;
                case 4:
                    category = new Category(
                            bundle.getString(NewCategoryActivity.EXTRA_NAME),
                            bundle.getString(NewCategoryActivity.EXTRA_DESCRIPTION),
                            UnsplashPic.createFromString(
                                    bundle.getString(NewCategoryActivity.EXTRA_UNSPLASHPICTURE)),
                            4);
                    mCategoryViewModel.insert(category);
                    break;
                default:
                    break;
            }
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
        finishAffinity();
    }

    @Override
    public void onCategorySelected(Category category) {
        Intent intent = new Intent(MainActivity.this, DrawerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Category", category.stringify());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (editingCategory){
            Bundle bundle = getIntent().getExtras()
                    .getBundle(DrawerActivity.EXTRA_BUNDLE_EDITED_CATEGORY);

            String temp = bundle.getString(EditCategoryActivity.EXTRA_NAME);
            mCategoryViewModel.update(new CategoryRepository.MyTaskParams(
                    bundle.getInt(EditCategoryActivity.EXTRA_ID),
                    bundle.getString(EditCategoryActivity.EXTRA_NAME),
                    bundle.getString(EditCategoryActivity.EXTRA_DESCRIPTION),
                    bundle.getInt(EditCategoryActivity.EXTRA_CATEGORYTYPE)));

            editingCategory = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.actionsearch_main) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //TODO deletion of category from database not working. Review this code and fix it
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof CategoryListAdapter.CategoryViewHolder) {
            // get the removed item nameTv to display it in snack bar
            String name = mRecyclerViewAdapter
                    .getCategories().get(viewHolder.getAdapterPosition()).getMName();

            // backup of removed skill for undo purpose
            final Category deletedCategory = mRecyclerViewAdapter
                    .getCategories().get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the category from recycler view
            mRecyclerViewAdapter.removeCategory(viewHolder.getAdapterPosition());

            // remove the category from the database
            mCategoryViewModel.delete(deletedCategory);

            //create backup table in case of restoring
//            mSkillViewModel.createBackupTable(deletedCategory.getMName());

            // drop table from database
//            query = new SimpleSQLiteQuery(String.format
//                    ("DROP %s", deletedCategory.getMName()));
//            mSkillViewModel.rawQuery(query);
//            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from the skills list!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted skill
                    mRecyclerViewAdapter.restoreCategories(deletedCategory, deletedIndex);
                    // Insert the deletedSkill in the database again
                    mCategoryViewModel.insert(deletedCategory);

//                    SimpleSQLiteQuery query = new SimpleSQLiteQuery(String.format
//                            ("CREATE TABLE %s AS" +
//                                    "SELECT *" +
//                                    "FROM backup", deletedCategory.getMName()));
//                    mSkillViewModel.rawQuery(query);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

            //delete the backup table after a few seconds
//            new java.util.Timer().schedule(
//                    new java.util.TimerTask() {
//                        @Override
//                        public void run() {
//                            SimpleSQLiteQuery query = new SimpleSQLiteQuery("DROP backup");
//                            mSkillViewModel.rawQuery(query);
//                        }
//                    },
//                    snackbar.getDuration()
//            );
        }
    }

    private void setFAB() {
        FloatingActionButton mFab = findViewById(R.id.fab_main);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =
                        new Intent (MainActivity.this, NewCategoryActivity.class);

                startActivityForResult(intent, NEW_SKILL_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }
}