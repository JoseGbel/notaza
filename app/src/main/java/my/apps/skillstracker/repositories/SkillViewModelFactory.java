package my.apps.skillstracker.repositories;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

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
