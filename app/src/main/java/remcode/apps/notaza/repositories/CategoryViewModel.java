package remcode.apps.notaza.repositories;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import java.util.List;

import remcode.apps.notaza.model.Category;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository categoryRepository;
    private LiveData<List<Category>> mAllCategories;

    public CategoryViewModel(Application application) {
        super(application);

        categoryRepository = new CategoryRepository(application);
        mAllCategories = categoryRepository.getAllCategories();
    }

    public LiveData<List<Category>> getAllCategories() { return mAllCategories; }
    public Category getCategoryById(int id) { return categoryRepository.getCategoryBy(id); }
    public void insert(Category category) { categoryRepository.insert(category); }
    public void delete(Category category) { categoryRepository.delete(category); }
    public void update(CategoryRepository.MyTaskParams params) {
        categoryRepository.update(params);
    }
}
