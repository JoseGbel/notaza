package my.apps.skillstracker.unsplashapi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Urls implements Parcelable{

    public Urls(String thumb) {
        this.thumb = thumb;
    }

    public static final Creator<Urls> CREATOR = new Creator<Urls>() {
        @Override
        public Urls createFromParcel(Parcel in) {
            return new Urls(in);
        }

        @Override
        public Urls[] newArray(int size) {
            return new Urls[size];
        }
    };

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    private String thumb;

    @Override
    public int describeContents() {
        return 0;
    }

    private Urls (Parcel in){
        this.thumb = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.thumb);
    }
}
