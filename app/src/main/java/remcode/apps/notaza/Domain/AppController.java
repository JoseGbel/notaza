package remcode.apps.notaza.Domain;
import com.google.android.play.core.install.model.AppUpdateType;

import com.google.android.play.core.install.model.AppUpdateType;

public class AppController {

    public UpdateManager updateManager;

    public AppController(){
        updateManager = new UpdateManager();
    }


}
