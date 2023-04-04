package client.scenes;

import client.utils.ErrorUtils;
import client.utils.ServerUtils;
import commons.Task;
import commons.Subtask;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import java.util.List;

public class SubtaskCtrl
        implements IEntityRepresentation<Subtask> {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;

    private Subtask subtask;
    private Task parentTask;

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
     * @param server the server of the application
     * @param mainCtrlTalio main controller of the application
     */
    @Inject
    public SubtaskCtrl(ServerUtils server,
                       MainCtrlTalio mainCtrlTalio) {
        this.server = server;
        this.mainCtrl = mainCtrlTalio;
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
        title.setText(subtask.getTitle());
        if (subtask.isCompleted()) {
            completed.setSelected(true);
        }
        else {
            completed.setSelected(false);
        }
    }

    /**
     * @param task task that holds this subtask
     */
    public void setParentTask(Task task) {
        this.parentTask = task;
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

        Image upIcon = new Image(getClass()
                .getResourceAsStream("/client/images/arrowUp.png"));
        Image downIcon = new Image(getClass()
                .getResourceAsStream("/client/images/arrowDown.png"));
        this.upIcon.setImage(upIcon);
        this.downIcon.setImage(downIcon);
    }


    /**
     * Used to delete a task from a list
     */
    public void deleteSubtask() {
        boolean confirmation = server.confirmDeletion("subtask");

        if (confirmation) {
            server.deleteSubtask(subtask);
            parentTask.removeSubtask(subtask);
            mainCtrl.showTaskDetails(parentTask);
        }
    }

    /**
     * Switches to the subtask's rename scene
     */
    public void edit() {
        mainCtrl.showRenameSubtask(subtask);
        mainCtrl.refreshBoard();
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
            ErrorUtils.alertError("You cannot move the subtask higher.");
            return;
        }
        currentSubtasks.remove(subtask);
        currentSubtasks.add(taskIndex, subtask);
        currentTask.setSubtasks(currentSubtasks);

        Task task = server.updateSubtasksInTask(currentTask, currentSubtasks);
        this.parentTask = task;

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
            ErrorUtils.alertError("You cannot move the subtask lower.");
            return;
        }
        currentSubtasks.remove(subtask);
        currentSubtasks.add(taskIndex, subtask);
        currentTask.setSubtasks(currentSubtasks);

        server.updateSubtasksInTask(currentTask, currentSubtasks);

        mainCtrl.showTaskDetails(parentTask);
    }

    /**
     * Updates the status of the current subtask
     */
    public void completeness() {
        boolean newValue = completed.isSelected();
        subtask.setCompleted(newValue);
        server.updateSubtaskCompleteness(subtask, newValue);
    }

}
