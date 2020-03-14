package remcode.apps.notaza.Domain;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentSender;
import android.util.Log;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import java.time.Duration;

import static android.app.Activity.RESULT_OK;


public class UpdateManager implements Updatable {

    private static final int MY_REQUEST_CODE = 100;

    private AppUpdateManager appUpdateManager;
    private Context context;

    UpdateManager(Context context){
        appUpdateManager = AppUpdateManagerFactory.create(context);
        this.context = context;
    }

    @Override
    public void checkUpdateAvailability(int updateType) {
        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(updateType)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            updateType,
                            // The current activity making the update request.
                            (Activity)context,
                            // Include a request code to later monitor this update request.
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void checkUpdateInProgress(int updateType) {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            updateType,
                                            (Activity)context,
                                            MY_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }

    @Override
    public void checkUpdateIsNotStalled(int updateType) {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.UPDATE_AVAILABLE) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            updateType,
                                            (Activity) context,
                                            MY_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }

    @Override
    public void checkUpdateResult(int requestCode, int resultCode) {
        if (requestCode == MY_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK: { }
                    break;
                case Activity.RESULT_CANCELED : { }
                    break;
                case ActivityResult.RESULT_IN_APP_UPDATE_FAILED: {
                    Log.e("UPDATE ERROR", "Update flow failed! Result code: " + resultCode);
                }
                    break;
            }
        }
    }
}
