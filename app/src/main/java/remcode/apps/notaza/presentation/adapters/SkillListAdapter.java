package remcode.apps.notaza.presentation.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import remcode.apps.notaza.R;
import remcode.apps.notaza.model.Skill;
import remcode.apps.notaza.utils.ParseExperience;

public class SkillListAdapter
        extends RecyclerView.Adapter<SkillListAdapter.SkillViewHolder> implements Filterable {

    private final LayoutInflater mInflater;
    private List<Skill> mSkills;
    private List<Skill> mSkillsFiltered;
    private SkillListAdapterListener listener;
    private Context context;

    public class SkillViewHolder extends RecyclerView.ViewHolder {

        LinearLayout skillLinearLayout;

        TextView nameTv, descriptionTv, dateTv, experienceTv;

        SkillViewHolder(View view){
            super(view);
            skillLinearLayout = view.findViewById(R.id.skill_linear_layout);
            nameTv = view.findViewById(R.id.skill_name);
            descriptionTv = view.findViewById(R.id.skill_description);
            dateTv = view.findViewById(R.id.skill_date);
            experienceTv = view.findViewById(R.id.skill_experience);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected skill in callback
                    try{
                        listener.onSkillSelected(mSkillsFiltered.get(getAdapterPosition()));
                    }catch (Exception e){
                        Log.e ("Boom", e.toString());
                    }
                }
            });
        }
    }

    public List<Skill> getSkills() { return mSkills; }

    public SkillListAdapter(Context context,
                            SkillListAdapterListener listener){

        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LinearLayout myLayout = (LinearLayout) mInflater.inflate(R.layout.skill_view_holder_layout,
                parent, false);

        return new SkillViewHolder(myLayout);
    }

    public void onBindViewHolder(@NonNull SkillViewHolder viewHolder, final int position) {

        if(mSkillsFiltered != null) {

            Skill currentSkill = mSkillsFiltered.get(position);

            //TODO implement different Dateformat for different locales
            String patternEU = "dd/MM/yyyy", patternUSA = "MM/dd/yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patternEU);

            viewHolder.nameTv.setText(currentSkill.getMName());
            viewHolder.descriptionTv.setText(currentSkill.getMDescription());
            setExperienceTextView(viewHolder, currentSkill);

            Date now = new Date();
            long oneDay = TimeUnit.HOURS.toMillis(24);

            if (now.getTime() - currentSkill.getMDate().getTime() <= oneDay)
                viewHolder.dateTv.setText(R.string.today);
            else
                viewHolder.dateTv.setText(simpleDateFormat.format(currentSkill.getMDate()));

        }else
            viewHolder.nameTv.setText(R.string.entrynotavailable);
    }

    private void setExperienceTextView(@NonNull SkillViewHolder viewHolder, Skill currentSkill) {

        if (currentSkill.getMExperience() == null)
            viewHolder.experienceTv.setText(R.string.empty_string);
        else {
            switch (currentSkill.getMExperience()) {
                case 1:
                    viewHolder.experienceTv.setText(R.string.novice);
                    break;
                case 2:
                    viewHolder.experienceTv.setText(R.string.beginner);
                    break;
                case 3:
                    viewHolder.experienceTv.setText(R.string.competent);
                    break;
                case 4:
                    viewHolder.experienceTv.setText(R.string.proficient);
                    break;
                case 5:
                    viewHolder.experienceTv.setText(R.string.expert);
                    break;
                default:
                    viewHolder.experienceTv.setText(R.string.empty_string);
            }
        }
    }

    public void removeSkill(int position) {
        getSkills().remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreSkill(Skill skill, int position) {
        getSkills().add(position, skill);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void setSkills(List<Skill> skills){
        mSkills = skills;
        mSkillsFiltered = skills;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        if (mSkillsFiltered != null)
            return mSkillsFiltered.size();
        else
            return 0;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                if (charString.isEmpty())
                    mSkillsFiltered = mSkills;
                else {
                    List<Skill> filteredList = new ArrayList<>();

                    for (Skill s : mSkills){
                        if (s.getMName().toLowerCase().contains(charString.toLowerCase()) ||
                                s.getMDescription().contains(charString.toLowerCase())    ||
                                ParseExperience.toString(s.getMExperience(), context)
                                        .toLowerCase()
                                        .contains(charString.toLowerCase())){
                            filteredList.add(s);
                        }
                    }

                    mSkillsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mSkillsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                mSkillsFiltered = (ArrayList<Skill>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface SkillListAdapterListener {
        void onSkillSelected(Skill skill);
    }
}
