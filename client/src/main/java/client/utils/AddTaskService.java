package client.utils;

import client.scenes.MainCtrlTalio;
import com.google.inject.Inject;
import commons.TaskList;

public class AddTaskService {
    private ServerUtils server;
    private MainCtrlTalio mainCtrl;
    private TaskList parentTaskList;

    /**
     * Constructor for the Addtask
     * @param server injects a server object
     * @param mainCtrl injects a mainCtrl object
     */
    @Inject
    public AddTaskService (ServerUtils server, MainCtrlTalio mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * @param parentTaskList task list to which the new task will be added
     */
    public void setParentTaskList(TaskList parentTaskList) {
        this.parentTaskList = parentTaskList;
    }

    /**
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
}