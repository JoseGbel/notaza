package my.apps.skillstracker.unsplashapi;

import android.content.Context;
import android.net.sip.SipSession;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import my.apps.skillstracker.R;
import my.apps.skillstracker.unsplashapi.model.UnsplashPic;

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
        viewHolder.artistName.setText(pic.getUser().getUsername());
        viewHolder.unsplash.setText(R.string.UnsplashStringName);

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
