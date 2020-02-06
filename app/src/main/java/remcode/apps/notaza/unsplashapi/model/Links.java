package remcode.apps.notaza.unsplashapi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Links implements Parcelable {

    private String html;

    public Links(String self) {
        this.html = self;
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

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Links (Parcel in){
        this.html = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.html);
    }
}
