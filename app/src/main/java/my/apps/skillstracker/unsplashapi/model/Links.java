package my.apps.skillstracker.unsplashapi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Links implements Parcelable {

    private String self;

    public Links(String self) {
        this.self = self;
    }

    public static final Creator<Links> CREATOR = new Creator<Links>() {
        @Override
        public Links createFromParcel(Parcel in) {
            return new Links(in);
        }

        @Override
        public Links[] newArray(int size) {
            return new Links[size];
        }
    };

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Links (Parcel in){
        this.self = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
    }
}
