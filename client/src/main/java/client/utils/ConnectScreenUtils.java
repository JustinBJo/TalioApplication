package client.utils;

import com.google.inject.Inject;

public class ConnectScreenUtils {

    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;

    /**
     * Constructor for the ConnectScreen service
     * @param mainCtrl the mainCtrl injected
     * @param server the server injected
     */
    @Inject
    public ConnectScreenUtils(MainCtrlTalio mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * setter for the server
     * @param address the server to be set
     */
   public void setServer(String address) {
       server.setServer(address);
   }

    /**
     * changes the current server address
     * @param address the address to be joined
     */
   public void changeServer(String address) {
       mainCtrl.changeServer(address);
       mainCtrl.showMain();
   }

    /**
     * getter for the mainCtrl
     * @return the mainCtrl used
     */
    public MainCtrlTalio getMainCtrl() {
        return mainCtrl;
    }

    /**
     * getter for the server
     * @return the server
     */
    public ServerUtils getServer() {
        return server;
    }
}
