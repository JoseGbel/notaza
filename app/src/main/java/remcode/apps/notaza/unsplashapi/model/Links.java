package remcode.apps.notaza.unsplashapi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Links implements Parcelable {

    private String download_location;
    private String html;

    public Links(String self) {
        this.download_location = self;
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

    public String getDownload_location() {
        return download_location;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void setDownload_location(String download_location) {
        this.download_location = download_location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Links (Parcel in){
        this.download_location = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.download_location);
    }
}
