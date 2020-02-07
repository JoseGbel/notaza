//package my.apps.skillstracker.utils;
//
//
//import android.arch.persistence.room.TypeConverter;
//
//import java.util.Date;
//
//import my.apps.skillstracker.unsplashapi.model.Links;
//
//public class LinksTypeConverter {
//
//    @TypeConverter
//    public static Links toLinks(String value) {
//        return value == null ? null : new Links(value);
//    }
//
//    @TypeConverter
//    public static String toString(Links value) {
//        return value == null ? null : value.getDownload_location();
//    }
//}
