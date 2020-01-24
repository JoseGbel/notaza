package my.apps.skillstracker.utils;


import android.arch.persistence.room.TypeConverter;

import my.apps.skillstracker.unsplashapi.model.Links;
import my.apps.skillstracker.unsplashapi.model.UnsplashPic;
import my.apps.skillstracker.unsplashapi.model.Urls;
import my.apps.skillstracker.unsplashapi.model.User;

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