package my.apps.skillstracker;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface SkillDao {

    @Insert
    void insert(Skill skill);

    @Delete
    void delete(Skill skill);

    // This method is kept in case we need to delete all te skills in the future
    @Query("DELETE FROM skill_table")
    void deleteAll();

    @Query("SELECT * FROM skill_table WHERE category = :category ORDER BY name ASC")
    LiveData<List<Skill>> getAllSkills(String category);

    @Query ("UPDATE skill_table SET name = :name, description = :description, experience = :experience WHERE id = :id")
    void update(int id, String name, String description, int experience);
}