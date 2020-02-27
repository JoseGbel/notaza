package remcode.apps.notaza.presentation.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import remcode.apps.notaza.R;
import remcode.apps.notaza.unsplashapi.model.UnsplashPic;

public class PicSelectionRecyclerViewAdapter extends RecyclerView.Adapter<PicSelectionRecyclerViewAdapter.ViewHolder> {

    private ArrayList<UnsplashPic> pictureList;
    private Context context;
    private RecyclerViewAdapterListener listener;

    public PicSelectionRecyclerViewAdapter(Context context, RecyclerViewAdapterListener listener){
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
        // todo make the link work
        Spanned artistLink = Html.fromHtml("<a href=\""
                + pic.getUser().getLinks().getHtml()
                + "\">"
                + pic.getUser().getName()
                + "</a>");
        Spanned unsplashLink = Html.fromHtml("<a href=\"https://unsplash.com/\">"
                + context.getString(R.string.UnsplashStringName)
                + "</a>");

        viewHolder.artistLink.setText(artistLink);
        viewHolder.unsplashLink.setText(unsplashLink);
        viewHolder.artistLink.setMovementMethod(LinkMovementMethod.getInstance());
        viewHolder.unsplashLink.setMovementMethod(LinkMovementMethod.getInstance());
        Glide.with(context)
                .load(pic.getUrls().getRegular())
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

    public void deletePictures() {
        this.pictureList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView artistLink;
        private TextView unsplashLink;

        ViewHolder(final View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.unsplash_imageview);
            artistLink = itemView.findViewById(R.id.artist_name);
            unsplashLink = itemView.findViewById(R.id.unsplash_name);
            imageView.setOnClickListener(v ->
                    listener.onPictureSelected(pictureList.get(getAdapterPosition()), itemView));
        }

    }

    public interface RecyclerViewAdapterListener {
        void onPictureSelected(UnsplashPic unsplashPic, View selectedView);
    }
}
