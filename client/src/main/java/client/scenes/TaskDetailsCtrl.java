package client.scenes;

import client.utils.ErrorUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Optional;

public class TaskDetailsCtrl {
    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;

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
     * @param server injects a server object
     * @param mainCtrl injects a mainCtrl object
     */
    @Inject
    public TaskDetailsCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
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

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Task?");
        alert.setHeaderText("Delete Task");
        alert.setContentText("Are you sure you want to delete this task?");

        ButtonType confirmButton = new ButtonType("Confirm");
        ButtonType cancelButton = new ButtonType("Cancel");

        // Remove the default buttons and add Confirm and Cancel buttons
        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        // Check the user's response and perform the desired action
        if (result.isPresent() && result.get() == confirmButton) {
            server.deleteTask(task);
            exit();
        }
    }

}
