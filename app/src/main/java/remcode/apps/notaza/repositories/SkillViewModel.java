package remcode.apps.notaza.repositories;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import remcode.apps.notaza.model.Skill;

public class SkillViewModel extends AndroidViewModel {

    private SkillRepository skillRepository;
    private LiveData<List<Skill>> mAllSkills;

    SkillViewModel(Application application, String category) {
        super(application);
        skillRepository = new SkillRepository(application, category);
        mAllSkills = skillRepository.getAllSkills(category);
    }

    public LiveData<List<Skill>> getAllSkills(String currentCategory) {
        skillRepository.getAllSkills(currentCategory);
        return mAllSkills;
    }

    public void insert(Skill skill) { skillRepository.insert(skill); }
    public void delete(Skill skill) { skillRepository.delete(skill); }

    public void update(SkillRepository.MyTaskParams params) {
        skillRepository.update(params);
    }
}
