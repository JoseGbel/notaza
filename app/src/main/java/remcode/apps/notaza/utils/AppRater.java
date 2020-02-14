package remcode.apps.notaza.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import remcode.apps.notaza.R;

public class AppRater {
    private final static String APP_TITLE = "Notaza";
    private final static String APP_PNAME = "remcode.apps.notaza";

    private final static int DAYS_UNTIL_PROMPT = 3;
    private final static int LAUNCHES_UNTIL_PROMPT = 3;

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.apply();
    }

    private static void showRateDialog(final Context mContext,
                                       final SharedPreferences.Editor editor) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);

        dialog.setTitle(mContext.getString(R.string.Rate) + APP_TITLE);
        dialog.setMessage(mContext.getString(R.string.enjoyUsing)
                + APP_TITLE + mContext.getString(R.string.pleaseRate));

        dialog.setPositiveButton(mContext.getString(R.string.Rate), (dialogInterface, i) -> {
            editor.putBoolean("dontshowagain", true);
            editor.commit();
            mContext.startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
        });

        dialog.setNeutralButton(mContext.getString(R.string.remindMeLater), (dialogInterface, i) -> {
        });

        dialog.setNegativeButton(mContext.getString(R.string.noThanks), (dialogInterface, i) -> {
            if (editor != null) {
                editor.putBoolean("dontshowagain", true);
                editor.commit();
            }
        });

        dialog.show();
    }
}