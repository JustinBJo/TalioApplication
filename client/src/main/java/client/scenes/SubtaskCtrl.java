package client.scenes;

import client.utils.AlertUtils;
import client.utils.EntityWebsocketManager;
import client.utils.WebsocketUtils;
import client.utils.ServerUtils;
import commons.Task;
import commons.Subtask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import java.util.Objects;
import java.util.List;

public class SubtaskCtrl
        implements IEntityRepresentation<Subtask> {

    private final WebsocketUtils websocket;
    private final AlertUtils alertUtils;
    private final MainCtrlTalio mainCtrl;

    private Subtask subtask;
    private Task parentTask;

    private final EntityWebsocketManager<Subtask> entityWebsocket;

    @FXML
    AnchorPane root;
    @FXML
    Label title;
    @FXML
    Button delete;
    @FXML
    Button moveUp;
    @FXML
    Button moveDown;
    @FXML
    CheckBox completed;

    @FXML
    private ImageView editIcon;
    @FXML
    private ImageView deleteIcon;
    @FXML
    ImageView upIcon;
    @FXML
    ImageView downIcon;

    /**
     * Main constructor for SubtaskCtrl
     *
     * @param mainCtrlTalio main controller of the application
     */
    @Inject
    public SubtaskCtrl(WebsocketUtils websocket,
                       AlertUtils alertUtils,
                       MainCtrlTalio mainCtrlTalio) {
        this.websocket = websocket;
        this.alertUtils = alertUtils;
        this.mainCtrl = mainCtrlTalio;

        this.entityWebsocket = new EntityWebsocketManager<>(
                websocket,
                "subtask",
                Subtask.class,
                this::setEntity
        );
    }

    /**
     * Set the Subtask entity that this controller holds
     * @param subtask the subtask that is being saved to this controller
     */
    public void setEntity(Subtask subtask) {
        this.subtask = subtask;
        if (subtask.getTitle() == null) {
            subtask.setTitle("Untitled");
        }

        Platform.runLater(() -> {
            title.setText(subtask.getTitle());
            completed.setSelected(subtask.isCompleted());
        });

        entityWebsocket.register(subtask.getId(), "update");
        entityWebsocket.register(subtask.getId(), "updateCompleteness");
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

        Image upIcon = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/arrowUp.png")));
        Image downIcon = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/arrowDown.png")));

        this.upIcon.setImage(upIcon);
        this.downIcon.setImage(downIcon);
    }


    /**
     * Used to delete a task from a list
     */
    public void deleteSubtask() {
        boolean confirmation = alertUtils.confirmDeletion("subtask");

        if (confirmation) {
            websocket.deleteSubtask(subtask);
            parentTask.removeSubtask(subtask);
            mainCtrl.showTaskDetails(parentTask);
        }
    }

    /**
     * Switches to the subtask's rename scene
     */
    public void edit() {
        mainCtrl.showRenameSubtask(subtask);
    }

    /**
     * Used to move a subtask up in the parent task
     */
    public void moveUp() {
        Task currentTask = parentTask;

        List<Subtask> currentSubtasks = currentTask.getSubtasks();
        int taskIndex = currentSubtasks.indexOf(subtask);
        taskIndex--;
        if (taskIndex < 0 || taskIndex >= currentSubtasks.size()) {
            alertUtils.alertError("You cannot move the subtask higher.");
            return;
        }
        currentSubtasks.remove(subtask);
        currentSubtasks.add(taskIndex, subtask);
        currentTask.setSubtasks(currentSubtasks);

        websocket.updateSubtasksInTask(currentTask, currentSubtasks);

        mainCtrl.showTaskDetails(parentTask);
    }

    /**
     * Used to move a subtask down in the parent task
     */
    public void moveDown() {
        Task currentTask = parentTask;

        List<Subtask> currentSubtasks = currentTask.getSubtasks();
        int taskIndex = currentSubtasks.indexOf(subtask);
        taskIndex++;
        if (taskIndex < 0 || taskIndex >= currentSubtasks.size()) {
            alertUtils.alertError("You cannot move the subtask lower.");
            return;
        }
        currentSubtasks.remove(subtask);
        currentSubtasks.add(taskIndex, subtask);
        currentTask.setSubtasks(currentSubtasks);

        websocket.updateSubtasksInTask(currentTask, currentSubtasks);

        mainCtrl.showTaskDetails(parentTask);
    }

    /**
     * Updates the status of the current subtask
     */
    public void completeness() {
        boolean newValue = completed.isSelected();
        subtask.setCompleted(newValue);
        websocket.updateSubtaskCompleteness(subtask, newValue);
    }
}
