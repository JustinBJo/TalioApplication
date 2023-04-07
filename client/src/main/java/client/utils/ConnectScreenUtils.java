package client.utils;

import client.scenes.MainCtrlTalio;
import com.google.inject.Inject;

public class ConnectScreenUtils {

    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;

    @Inject
    public ConnectScreenUtils(MainCtrlTalio mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

   public void setServer(String address) {
       server.setServer(address);
   }
   public void changeServer(String address) {
       mainCtrl.changeServer(address);
       mainCtrl.showMain();
   }

    public MainCtrlTalio getMainCtrl() {
        return mainCtrl;
    }

    public ServerUtils getServer() {
        return server;
    }
}
