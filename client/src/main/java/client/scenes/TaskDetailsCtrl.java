package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.IOException;

public class TaskDetailsCtrl {
    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;
    private static ServerUtils serverCopy;
    private static MainCtrlTalio mainCtrlTalioCopy;
    private Task task;

    @FXML
    private Label title;

    @FXML
    private Label description;

    /**
     * Empty constructor
     */
    public TaskDetailsCtrl() {
        if (serverCopy != null) {
            this.server = serverCopy;
            this.mainCtrl = mainCtrlTalioCopy;
            this.task = mainCtrl.getCurrentTask();
        }
        else {
            this.server = null;
            this.mainCtrl = null;
        }

    }

    /**
     * Constructor for the task details
     * @param server injects a server object
     * @param mainCtrl injects a mainCtrl object
     */
    @Inject
    public TaskDetailsCtrl(ServerUtils server, MainCtrlTalio mainCtrl,
                           TaskListCtrl taskListCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;

        this.serverCopy = server;
        this.mainCtrlTalioCopy = mainCtrl;
        this.task = mainCtrl.getCurrentTask();
    }

    /**
     * Method exit for exiting the detailed view of a task
     * returns to main scene
     */
    public void exit() {
        mainCtrl.mainSceneCtrl.refresh();
        mainCtrl.showMain();
    }

    /**
     * Edit a task
     * @throws IOException -
     */
    public void editTask() throws IOException {
        Task currentTask = task;
        mainCtrl.showEditTask(currentTask);
    }

    /**
     * Deletes the task from the detailed view and returns to the main scene
     */
    public void deleteTask() {
        Task currentTask = task;
        server.deleteTask(currentTask);
        mainCtrl.mainSceneCtrl.refresh();
        mainCtrl.showMain();
    }
}
