package my.apps.skillstracker.utils;

import android.content.Context;

import my.apps.skillstracker.R;

public class ParseExperience {

    public static String toString (Integer integer, Context context){

        if (integer == null)
            return context.getResources().getString(R.string.empty_string);
        else{
            switch (integer){
                case 1:
                    return context.getResources().getString(R.string.novice);
                case 2:
                    return context.getResources().getString(R.string.beginner);
                case 3:
                    return context.getResources().getString(R.string.competent);
                case 4:
                    return context.getResources().getString(R.string.proficient);
                case 5:
                    return context.getResources().getString(R.string.expert);
                default:
                    return context.getResources().getString(R.string.empty_string);
            }
        }
    }
}
