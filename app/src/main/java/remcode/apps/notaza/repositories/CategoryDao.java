package remcode.apps.notaza.repositories;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

import remcode.apps.notaza.model.Category;
import remcode.apps.notaza.unsplashapi.model.UnsplashPic;

@Dao
public interface CategoryDao {

    @Insert
    void insert(Category category);

    @Delete
    void delete(Category category);

    // This method is kept in case deletion of all categories in the future is needed
    @Query("DELETE FROM category_table")
    void deleteAll();

    @Query("SELECT * from category_table ORDER BY name ASC")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT * from category_table WHERE id = :id")
    Category getCategory(int id);

    @Query ("UPDATE category_table " +
            "SET name = :name, description = :description, type = :type, picture = :picture " +
            "WHERE id = :id")
    void update(int id, String name, String description, int type, UnsplashPic picture);
}
