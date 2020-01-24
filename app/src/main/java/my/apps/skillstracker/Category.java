package my.apps.skillstracker;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import my.apps.skillstracker.unsplashapi.model.UnsplashPic;

@Entity (tableName = "category_table")
public class Category {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "picture")
    private UnsplashPic mPicture;

    @ColumnInfo(name = "type")
    private Integer mType;

    public UnsplashPic getMPicture() {
        return mPicture;
    }

    public void setMPicture(UnsplashPic mPicture) {
        this.mPicture = mPicture;
    }

    public Category(@NonNull String mName, String mDescription, UnsplashPic mPicture, Integer mType) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mPicture = mPicture;
        this.mType = mType;
    }

    public void setMId (int id) { mId = id ; }

    public int getMId() {
        return mId;
    }

    public Integer getMType() { return mType; }

    @NonNull
    public String getMName() {
        return mName;
    }

    public String getMDescription() {
        return mDescription;
    }

    public static Category createFromString(String string) {
        return new Gson().fromJson(string, Category.class);

    }

    public String stringify() {
        return new Gson().toJson(this, Category.class);
    }
}
