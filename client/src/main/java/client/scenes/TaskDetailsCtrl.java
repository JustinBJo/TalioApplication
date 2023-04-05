package client.scenes;

import client.utils.ChildrenManager;
import client.utils.ErrorUtils;
import client.utils.ServerUtils;
import client.utils.TaskDetailsUtils;
import com.google.inject.Inject;
import commons.Subtask;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;


public class TaskDetailsCtrl {
//    private final MainCtrlTalio mainCtrl;
//    private final ServerUtils server;
//
//    private ChildrenManager<Subtask, SubtaskCtrl> subtaskChildrenManager;
//
//    private Task task;

    private final TaskDetailsUtils utils;
    @FXML
    private Label title;
    @FXML
    private Label description;
    @FXML
    private ImageView editIcon;
    @FXML
    private ImageView deleteIcon;
    @FXML
    VBox subtaskContainer;

    /**
     * Constructor for the task details
     * @param server injects a server object
     * @param mainCtrl injects a mainCtrl object
     */
    @Inject
    public TaskDetailsCtrl(TaskDetailsUtils utils) {
        this.utils = utils;
    }

    /**
     * This is called only once by the FXML builder,
     * after FXML components are initialized.
     */
    public void initialize() {
        Image editIcon = new Image(getClass()
                .getResourceAsStream("/client/images/editicon.png"));
        this.editIcon.setImage(editIcon);

        Image deleteIcon = new Image(getClass()
                .getResourceAsStream("/client/images/deleteicon.png"));
        this.deleteIcon.setImage(deleteIcon);

        utils.initialize(subtaskContainer);
    }

    /**
     * Updates this task's subtasks
     */
    public void refresh() {
        utils.refresh();
    }

    /**
     * @param task task whose details are shown in the scene
     */
    public void setTask(Task task) {
        utils.setTask(task);

        title.setText(task.getTitle());
        description.setText(task.getDescription());
    }

    /**
     * Method exit for exiting the detailed view of a task
     * returns to main scene
     */
    public void exit() {
        utils.exit();
    }

    /**
     * Edit a task
     */
    public void editTask() {
        utils.editTask();
    }

    /**
     * Deletes the task from the detailed view and returns to the main scene
     */
    public void deleteTask() {
        utils.deleteTask();
    }

    /**
     * Adds a subtask to the current task
     */
    public void addSubtask() {
        utils.addSubtask(); }

}
