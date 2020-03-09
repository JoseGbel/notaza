package remcode.apps.notaza.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;
import java.util.List;
import remcode.apps.notaza.model.Category;
import remcode.apps.notaza.unsplashapi.model.UnsplashPic;

public class CategoryRepository {

    private CategoryDao mCategoryDao;
    private LiveData<List<Category>> mAllCategories;

    CategoryRepository(Application application){
        SkillRoomDatabase db = SkillRoomDatabase.getDatabase(application);
        mCategoryDao = db.categoryDao();
        mAllCategories = mCategoryDao.getAllCategories();
    }

    LiveData<List<Category>> getAllCategories(){
        return mAllCategories;
    }

    Category getCategoryBy(int id){ return mCategoryDao.getCategory(id); }

    public void insert (Category category) {
        new insertAsyncTask(mCategoryDao).execute(category);
    }

    public void delete(Category category) {
        new deleteAsyncTask(mCategoryDao).execute(category);
    }

    public void update(MyTaskParams params) { new updateAsyncTask(mCategoryDao).execute(params); }

    // AsyncTask implementations
    private static class deleteAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDao mAsyncTaskDao;

        deleteAsyncTask(CategoryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Category... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<MyTaskParams, Void, Void> {

        private CategoryDao mAsyncTaskDao;

        updateAsyncTask(CategoryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MyTaskParams... params) {
            mAsyncTaskDao.update(params[0].getId(),
                    params[0].getName(),
                    params[0].getDescription(),
                    params[0].getType(),
                    params[0].getPicture());
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDao mAsyncTaskDao;

        insertAsyncTask(CategoryDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Category... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public static class MyTaskParams {

        int id;
        String name;
        String description;
        int type;
        UnsplashPic pic;

        public MyTaskParams(int id, String name, String description, int type, UnsplashPic pic) {
            this.name = name;
            this.description = description;
            this.id = id;
            this.type = type;
            this.pic = pic;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getType() {
            return type;
        }

        public UnsplashPic getPicture() {
            return pic;
        }
    }
}
