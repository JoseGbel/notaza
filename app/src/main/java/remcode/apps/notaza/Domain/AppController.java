package remcode.apps.notaza.Domain;
import android.content.Context;

import com.google.android.play.core.install.model.AppUpdateType;

import com.google.android.play.core.install.model.AppUpdateType;

public class AppController {

    public UpdateManager updateManager;

    public AppController(Context context){
        updateManager = new UpdateManager(context);
    }


}
