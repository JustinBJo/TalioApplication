package client.utils;

import com.google.inject.Inject;

public class EditTaskUtils {
    private MainCtrlTalio mainCtrl;
    private ServerUtils server;

    /**
     * Constructor for the EditTaskService
     * @param server injects a server object
     * @param mainCtrl injects a mainCtrl object
     */
    @Inject
    public EditTaskUtils(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Method cancel for cancelling the editing of a task
     * returns to main scene
     */
    public void cancel() {
        mainCtrl.showMain();
    }

    /**
     * Getter for the main ctrl
     * @return the main ctrl
     */
    public MainCtrlTalio getMainCtrl() {
        return mainCtrl;
    }

    /**
     * Getter for the server
     * @return the server
     */
    public ServerUtils getServer() {
        return server;
    }

    /**
     * Setter for the main ctrl
     * @param mainCtrl the main ctrl
     */
    public void setMainCtrl(MainCtrlTalio mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Setter for the server
     * @param server the server
     */
    public void setServer(ServerUtils server) {
        this.server = server;
    }

}
