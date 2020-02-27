package remcode.apps.notaza.model;

import java.util.Date;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "skill_table")
public class Skill {

    @ColumnInfo(name = "date")
    private Date mDate;

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "category")
    private String mCategory;

    @ColumnInfo(name = "experience")
    private Integer mExperience;

    public Skill(String mName, String mDescription, Integer mExperience, String mCategory) {
        this.mName = mName;
        this.mDescription = mDescription;
        mDate = new Date();
        this.mExperience = mExperience;
        this.mCategory = mCategory;
    }

    public void setMId (int id) { mId = id ; }

    public void setMDate(Date mDate) {
        this.mDate = mDate;
    }

    public int getMId() {
        return mId;
    }

    @NonNull
    public String getMName() {
        return mName;
    }

    public String getMDescription() {
        return mDescription;
    }

    public Date getMDate() {
        return mDate;
    }

    public Integer getMExperience() {
        return mExperience;
    }

    public String getMCategory() {
        return mCategory;
    }
}
