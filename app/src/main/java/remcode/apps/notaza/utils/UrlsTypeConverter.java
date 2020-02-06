//package my.apps.skillstracker.utils;
//
//
//import android.arch.persistence.room.TypeConverter;
//
//import my.apps.skillstracker.unsplashapi.model.Links;
//import my.apps.skillstracker.unsplashapi.model.Urls;
//import my.apps.skillstracker.unsplashapi.model.User;
//
//public class UrlsTypeConverter {
//
//    @TypeConverter
//    public static Urls toUrls(String value) {
//        return value == null ? null : new Urls(value);
//    }
//
//    @TypeConverter
//    public static String toString(Urls value) {
//        return value == null ? null : value.getThumb();
//    }
//}
