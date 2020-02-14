package remcode.apps.notaza.Domain;

import android.content.Context;

public interface Updatable {

    void checkUpdateAvailability(int updateType);
    void checkUpdateIsNotStalled(int updateType);
    void checkUpdateInProgress(int updateType);

    void checkUpdateResult(int requestCode, int resultCode);
}
