package client.scenes;

import client.utils.*;
import com.google.inject.Inject;
import commons.Task;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.util.Objects;


public class TaskDetailsCtrl {

    private final TaskDetailsUtils utils;
    private final ServerUtils serverUtils;
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
     * injector constructor
     * @param utils the service used for logic
     */
    @Inject
    public TaskDetailsCtrl(TaskDetailsUtils utils, ServerUtils serverUtils) {
        this.utils = utils;
        this.serverUtils = serverUtils;
    }

    /**
     * This is called only once by the FXML builder,
     * after FXML components are initialized.
     */
    public void initialize() {
        // Set up button icons
        Image editIcon = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/editicon.png")));
        this.editIcon.setImage(editIcon);

        Image deleteIcon = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/deleteicon.png")));
        this.deleteIcon.setImage(deleteIcon);

        utils.initialize(subtaskContainer, this::setEntity);

    }


    /**
     * @param task task whose details are shown in the scene
     */
    public void setEntity(Task task) {

        //serverUtils.resetTask(task.getId());

        utils.setEntity(task);

        Platform.runLater(() -> {
            title.setText(task.getTitle());
            description.setText(task.getDescription());
        });


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
       utils.addSubtask();
    }

}
