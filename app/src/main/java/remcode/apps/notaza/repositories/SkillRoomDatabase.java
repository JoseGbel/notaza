package remcode.apps.notaza.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;

import remcode.apps.notaza.model.Category;
import remcode.apps.notaza.model.Skill;
import remcode.apps.notaza.utils.DateTypeConverter;
import remcode.apps.notaza.utils.UnsplashPicTypeConverter;

//TODO clean up this class to be more readable
@Database(entities = {Skill.class, Category.class}, version = 7)
@TypeConverters({DateTypeConverter.class, /* LinksTypeConverter.class,
        UrlsTypeConverter.class, UserTypeConverter.class*/ UnsplashPicTypeConverter.class})

public abstract class SkillRoomDatabase extends RoomDatabase {

    public abstract SkillDao skillDao();
    public abstract CategoryDao categoryDao();

    private static final String DATABASE_NAME = "skill_database";

    private static volatile SkillRoomDatabase INSTANCE;

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    static SkillRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (SkillRoomDatabase.class){

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SkillRoomDatabase.class, DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .addMigrations(MIGRATION_4_5)
                            .addMigrations(MIGRATION_5_6)
                            .addMigrations(MIGRATION_6_7)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final SkillDao mDao;

        PopulateDbAsync(SkillRoomDatabase db) {
            mDao = db.skillDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            //To repopulate a clean database uncomment next line of code
            //mDao.deleteAll();
            return null;
        }
    }

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE skill_table "
                    + " ADD COLUMN experience INTEGER");
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE category_table "
                    + " ADD COLUMN picture TEXT");
        }
    };

    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE category_table "
                    + " ADD COLUMN type INTEGER");
        }
    };

}
