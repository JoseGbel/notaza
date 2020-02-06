package remcode.apps.notaza.Domain;

import android.content.Context;

public interface Updatable {

    void checkUpdateAvailability(Context context, int updateType);
    void checkUpdateInProgress(Context context, int updateType);

    void checkUpdateResult(int requestCode, int resultCode);
    void checkUpdateIsNotStalled(Context context);
}
