package client.scenes;

import client.utils.AlertUtils;
import client.utils.ServerUtils;
import client.utils.WebsocketUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;


public class TaskDetailsCtrl {
    private final MainCtrlTalio mainCtrl;
    private final WebsocketUtils websocket;
    private final AlertUtils alertUtils;

    private Task task;

    @FXML
    private Label title;
    @FXML
    private Label description;
    @FXML
    private ImageView editIcon;
    @FXML
    private ImageView deleteIcon;

    /**
     * Constructor for the task details
     * @param mainCtrl injects a mainCtrl object
     */
    @Inject
    public TaskDetailsCtrl(WebsocketUtils websocket, MainCtrlTalio mainCtrl, AlertUtils alertUtils) {
        this.alertUtils = alertUtils;
        this.websocket = websocket;
        this.mainCtrl = mainCtrl;
    }

    /**
     * This is called only once by the FXML builder,
     * after FXML components are initialized.
     */
    public void initialize() {
        Image editIcon = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/editicon.png")));
        this.editIcon.setImage(editIcon);

        Image deleteIcon = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/deleteicon.png")));
        this.deleteIcon.setImage(deleteIcon);
    }

    /**
     * @param task task whose details are shown in the scene
     */
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
            alertUtils.alertError("No task to edit!");
            exit();
        }
        mainCtrl.showEditTask(task);
    }

    /**
     * Deletes the task from the detailed view and returns to the main scene
     */
    public void deleteTask() {
        if (task == null) {
            alertUtils.alertError("No task to delete!");
            exit();
        }

        boolean confirmation = alertUtils.confirmDeletion("task");

        // Check the user's response and perform the desired action
        if (confirmation) {
            websocket.deleteTask(task);
            exit();
        }
    }

}
