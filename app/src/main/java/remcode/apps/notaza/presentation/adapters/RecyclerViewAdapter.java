package remcode.apps.notaza.presentation.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import remcode.apps.notaza.R;
import remcode.apps.notaza.unsplashapi.model.UnsplashPic;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<UnsplashPic> pictureList;
    private Context context;
    private RecyclerViewAdapterListener listener;

    public RecyclerViewAdapter (Context context, RecyclerViewAdapterListener listener){
        pictureList = new ArrayList<>();
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.picture_api_view_holder, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        UnsplashPic pic = pictureList.get(i);

        Spanned artistLink = Html.fromHtml("<a href=\"" + pic.getUser().getLinks().getHtml() + "\">" +
                pic.getUser().getUsername() + "</a>");
        Spanned unsplashLink = Html.fromHtml("<a href=\"https://unsplash.com/\">" +
                context.getString(R.string.UnsplashStringName) + "</a>");
//        viewHolder.artistName.setText(pic.getUser().getUsername());
        viewHolder.artistName.setText(artistLink);
        viewHolder.unsplash.setText(unsplashLink);
        Glide.with(context)
                .load(pic.getUrls().getThumb())
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }

    public void addPictures(ArrayList<UnsplashPic> pictureList) {

        this.pictureList.addAll(pictureList);
        notifyDataSetChanged();
    }

    public void clearList(){
        pictureList.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView artistName;
        private TextView unsplash;

        public ViewHolder (final View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.unsplash_imageview);
            artistName = itemView.findViewById(R.id.artist_name);
            unsplash = itemView.findViewById(R.id.unsplash_name);

//            artistName.setMovementMethod(LinkMovementMethod.getInstance());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPictureSelected(pictureList.get(getAdapterPosition()), itemView);
                }
            });
        }

    }

    public interface RecyclerViewAdapterListener {
        void onPictureSelected(UnsplashPic unsplashPic, View selectedView);
    }
}
