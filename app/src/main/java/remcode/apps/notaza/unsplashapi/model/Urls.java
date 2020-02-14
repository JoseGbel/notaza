package remcode.apps.notaza.unsplashapi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Urls implements Parcelable{

    private String thumb;

    private Urls (Parcel in){

        this.thumb = in.readString();
    }

    public Urls(String thumb) {

        this.thumb = thumb;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.thumb);
    }
}
