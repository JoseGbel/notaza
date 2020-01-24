package my.apps.skillstracker.unsplashapi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class UnsplashPic implements Parcelable {

    private String id;
    User user;
    Urls urls;

    public static final Creator<UnsplashPic> CREATOR = new Creator<UnsplashPic>() {
        @Override
        public UnsplashPic createFromParcel(Parcel in) {
            return new UnsplashPic(in);
        }

        @Override
        public UnsplashPic[] newArray(int size) {
            return new UnsplashPic[size];
        }
    };

    public Urls getUrls() {
        return urls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UnsplashPic(String id, Urls urls, User user){
        this.id = id;
        this.user = user;
        this.urls = urls;
    }

    UnsplashPic (Parcel in){

        Parcelable[] data = new Parcelable[2];

        this.id = in.readString();
        data[0] = in.readParcelable(User.class.getClassLoader());
        data[1] = in.readParcelable(Urls.class.getClassLoader());
        this.user = (User) data[0];
        this.urls = (Urls) data[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelableArray(new Parcelable[]{this.user, this.urls}, flags);
    }

    public static UnsplashPic createFromString(String string) {
        return new Gson().fromJson(string, UnsplashPic.class);

    }

    public String stringify() {
        return new Gson().toJson(this, UnsplashPic.class);
    }
}
