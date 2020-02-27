package remcode.apps.notaza.utils;


import androidx.room.TypeConverter;

import remcode.apps.notaza.unsplashapi.model.UnsplashPic;

public class UnsplashPicTypeConverter {

    @TypeConverter
    public static UnsplashPic toUnsplashPic(String stringified) {
        return UnsplashPic.createFromString(stringified);
    }

    @TypeConverter
    public static String toString(UnsplashPic value) {
        return value.stringify();
    }
}