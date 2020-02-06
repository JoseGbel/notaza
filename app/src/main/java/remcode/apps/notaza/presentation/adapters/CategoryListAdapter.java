package remcode.apps.notaza.presentation.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import remcode.apps.notaza.model.Category;
import remcode.apps.notaza.R;

public class CategoryListAdapter
        extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> implements Filterable {

    private final LayoutInflater mInflater;
    private List<Category> mCategories;
    private List<Category> mCategoriesFiltered;
    private CategoryListAdapterListener listener;
    private Context context;

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        CardView categoryCardView;
        TextView nameTv, descriptionTv;
        ImageView imageView;

        CategoryViewHolder(View view){
            super(view);
            categoryCardView = view.findViewById(R.id.category_cardview);
            nameTv = view.findViewById(R.id.category_name);
            descriptionTv = view.findViewById(R.id.category_description);
            imageView = view.findViewById(R.id.category_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected skill in callback
                    try {
                        listener.onCategorySelected(mCategoriesFiltered.get(getAdapterPosition()));
                    } catch (Exception e) {
                        Log.e("Boom", e.toString());
                    }
                }
            });
        }
    }

    public List<Category> getCategories() { return mCategories; }

    public CategoryListAdapter(Context context,
                               CategoryListAdapterListener listener){

        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = mInflater.inflate(R.layout.category_view_holder_layout,
                parent, false);

        return new CategoryViewHolder(v);
    }

    public void onBindViewHolder(@NonNull CategoryViewHolder viewHolder, final int position) {

        if(mCategoriesFiltered != null) {

            viewHolder.categoryCardView.setPadding(15, 5, 15, 5);
            viewHolder.categoryCardView.setRadius(15);
            Category currentCategory = mCategoriesFiltered.get(position);
            viewHolder.nameTv.setText(currentCategory.getMName());
            viewHolder.descriptionTv.setText(currentCategory.getMDescription());
            if (currentCategory.getMPicture() != null){
                if (currentCategory.getMPicture().getUrls() != null){
                    if (currentCategory.getMPicture().getUrls().getThumb() != null){
                        Glide.with(context)
                                .load(currentCategory.getMPicture().getUrls().getThumb())
                                .into(viewHolder.imageView);
                    }
                }
            }
        }else
            viewHolder.nameTv.setText(R.string.skillnotavailable);
    }

    public void removeCategory(int position) {
        getCategories().remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreCategories(Category category, int position) {
        getCategories().add(position, category);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void setCategories(List<Category> categories){
        mCategories = categories;
        mCategoriesFiltered = categories;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        if (mCategoriesFiltered != null)
            return mCategoriesFiltered.size();
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
                    mCategoriesFiltered = mCategories;
                else {
                    List<Category> filteredList = new ArrayList<>();

                    for (Category c : mCategories){
                        if (c.getMName()
                                .toLowerCase()
                                .contains(charString.toLowerCase()) ||
                            c.getMDescription()
                                    .toLowerCase()
                                    .contains(charString.toLowerCase())){
                            filteredList.add(c);
                        }
                    }

                    mCategoriesFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mCategoriesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                mCategoriesFiltered = (ArrayList<Category>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CategoryListAdapterListener {
        void onCategorySelected(Category category);
    }
}
