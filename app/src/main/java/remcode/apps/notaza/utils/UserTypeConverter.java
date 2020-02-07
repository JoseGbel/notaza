//package my.apps.skillstracker.utils;
//
//
//import android.arch.persistence.room.TypeConverter;
//
//import my.apps.skillstracker.unsplashapi.model.Links;
//import my.apps.skillstracker.unsplashapi.model.User;
//
//public class UserTypeConverter {
//
//    @TypeConverter
//    public static User toUser(String value, Links l) {
//        return value == null ? null : new User(value, l);
//    }
//
//    @TypeConverter
//    public static String toString(User value) {
//        return value == null ? null : value.getName();
//    }
//}
