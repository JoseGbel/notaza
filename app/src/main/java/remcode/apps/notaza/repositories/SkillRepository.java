package remcode.apps.notaza.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import remcode.apps.notaza.model.Skill;

public class SkillRepository {

    private SkillDao mSkillDao;
    private LiveData<List<Skill>> mAllSkills;

    SkillRepository(Application application, String category){

        SkillRoomDatabase db = SkillRoomDatabase.getDatabase(application);
        mSkillDao = db.skillDao();
        mAllSkills = mSkillDao.getAllSkills(category);
    }

    LiveData<List<Skill>> getAllSkills(String category){

        mAllSkills = mSkillDao.getAllSkills(category);
        return mAllSkills;
    }

    public void insert (Skill skill) {
        new insertAsyncTask(mSkillDao).execute(skill);
    }

    public void delete(Skill skill) {
        new deleteAsyncTask(mSkillDao).execute(skill);
    }

    public void update(MyTaskParams params) { new updateAsyncTask(mSkillDao).execute(params); }

    private static class deleteAsyncTask extends AsyncTask<Skill, Void, Void> {

        private SkillDao mAsyncTaskDao;

        deleteAsyncTask(SkillDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Skill... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    public static class MyTaskParams {

        int id;
        String name;
        String description;
        int experience;

        public MyTaskParams(int id, String name, String description, int experience) {
            this.name = name;
            this.description = description;
            this.id = id;
            this.experience = experience;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getExperience() { return experience; }
    }

    private static class updateAsyncTask extends AsyncTask<MyTaskParams, Void, Void> {

        private SkillDao mAsyncTaskDao;

        updateAsyncTask(SkillDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MyTaskParams... params) {
            mAsyncTaskDao.update(params[0].getId(),
                    params[0].getName(),
                    params[0].getDescription(),
                    params[0].getExperience());
            return null;
        }
    }


    private static class insertAsyncTask extends AsyncTask<Skill, Void, Void> {

        private SkillDao mAsyncTaskDao;

        insertAsyncTask(SkillDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Skill... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
