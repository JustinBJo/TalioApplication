package client.utils;

import com.google.inject.Inject;
import commons.TaskList;

public class AddTaskUtils {
    private ServerUtils server;
    private MainCtrlTalio mainCtrl;
    private TaskList parentTaskList;

    /**
     * Constructor for the AddTaskUtils
     * @param server injects a server object
     * @param mainCtrl injects a mainCtrl object
     */
    @Inject
    public AddTaskUtils (ServerUtils server, MainCtrlTalio mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * setter for the parent task list
     * @param parentTaskList task list to which the new task will be added
     */
    public void setParentTaskList(TaskList parentTaskList) {
        this.parentTaskList = parentTaskList;
    }

    /**
     * getter for the parent task list
     * @return the parent task list
     */
    public TaskList getParentTaskList() {
        return this.parentTaskList;
    }

    /**
     * setter for the server utils
     * @param server the server utils
     */
    public void setServer(ServerUtils server) {
        this.server = server;
    }

    /**
     * getter for the server utils
     * @return the server utils
     */
    public ServerUtils getServer() {
        return server;
    }

    /**
     * getter for the main ctrl
     * @return the main ctrl
     */
    public MainCtrlTalio getMainCtrl() {
        return mainCtrl;
    }

    /**
     * setter for the main ctrl
     * @param mainCtrl the main ctrl
     */
    public void setMainCtrl(MainCtrlTalio mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

}