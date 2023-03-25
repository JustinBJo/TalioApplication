package client.scenes;

import client.utils.ErrorUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.IOException;

public class TaskDetailsCtrl {
    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;

    private Task task;

    @FXML
    private Label title;
    @FXML
    private Label description;

    /**
     * Constructor for the task details
     * @param server injects a server object
     * @param mainCtrl injects a mainCtrl object
     */
    @Inject
    public TaskDetailsCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setTask(Task task) {
        if (task == null) return;

        this.task = task;

        title.setText(task.getTitle());
        description.setText(task.getDescription());
    }

    /**
     * Method exit for exiting the detailed view of a task
     * returns to main scene
     */
    public void exit() {
        mainCtrl.showMain();
    }

    /**
     * Edit a task
     */
    public void editTask() {
        if (task == null) {
            ErrorUtils.alertError("No task to edit!");
            exit();
        }
        mainCtrl.showEditTask(task);
    }

    /**
     * Deletes the task from the detailed view and returns to the main scene
     */
    public void deleteTask() {
        if (task == null) {
            ErrorUtils.alertError("No task to delete!");
            exit();
        }
        server.deleteTask(task);
        exit();
    }

}
