package remcode.apps.notaza.unsplashapi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class UnsplashPic implements Parcelable {

    private String id;
    private Urls urls;
    private User user;
    private Links links;

    public UnsplashPic(String id, Urls urls, User user, Links links){
        this.id = id;
        this.urls = urls;
        this.user = user;
        this.links = links;
    }

    UnsplashPic (Parcel in){

        Parcelable[] data = new Parcelable[3];

        this.id = in.readString();
        data[0] = in.readParcelable(User.class.getClassLoader());
        data[1] = in.readParcelable(Urls.class.getClassLoader());
        data[2] = in.readParcelable(Links.class.getClassLoader());
        this.user  = (User) data[0];
        this.urls  = (Urls) data[1];
        this.links = (Links)data[2];
    }

    public String getId() {
        return id;
    }

    public Urls getUrls() {
        return urls;
    }

    public Links getLinks() { return links; }

    public void setId(String id) {
        this.id = id;
    }

    public void setLinks(Links links) { this.links = links; }

    public User getUser() { return user; }

    public void setUser(User user) {
        this.user = user;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.links, flags);
        dest.writeParcelableArray(new Parcelable[]{this.user, this.urls}, flags);
    }

    public static UnsplashPic createFromString(String string) {
        return new Gson().fromJson(string, UnsplashPic.class);
    }

    public String stringify() {
        return new Gson().toJson(this, UnsplashPic.class);
    }
}
