package remcode.apps.notaza.repositories;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SkillViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private String mParam;

    public SkillViewModelFactory(Application application, String category) {

        mApplication = application;
        mParam = category;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new SkillViewModel(mApplication, mParam);
    }
}
