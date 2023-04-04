package client.scenes;

import client.utils.ChildrenManager;
import client.utils.ErrorUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Subtask;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;


public class TaskDetailsCtrl {
    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;

    private ChildrenManager<Subtask, SubtaskCtrl> subtaskChildrenManager;

    private Task task;

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

        subtaskContainer.getChildren().clear();
        this.subtaskChildrenManager =
                new ChildrenManager<>(
                        subtaskContainer,
                        SubtaskCtrl.class,
                        "Subtask.fxml"
                );
    }

    /**
     * Updates this task's subtasks
     */
    public void refresh() {
        subtaskContainer.getChildren().clear();
        this.subtaskChildrenManager = new ChildrenManager<>(
                subtaskContainer,
                SubtaskCtrl.class,
                "Subtask.fxml"
        );
        if (task == null) {
            // no children if there's no task list
            subtaskChildrenManager.updateChildren(new ArrayList<>());
        }
        var subtasks = server.getTaskData(task);
        subtaskContainer.getChildren().clear();
        this.subtaskChildrenManager =
                new ChildrenManager<>(
                        subtaskContainer,
                        SubtaskCtrl.class,
                        "Subtask.fxml"
                );
        subtaskChildrenManager.updateChildren(subtasks);

        for (SubtaskCtrl subtaskCtrl :
                subtaskChildrenManager.getChildrenCtrls()) {
            subtaskCtrl.setParentTask(task);
        }
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

        boolean confirmation = server.confirmDeletion("task");

        // Check the user's response and perform the desired action
        if (confirmation) {
            List<Subtask> subtasks = task.getSubtasks();
            for (Subtask subtask : subtasks)
                server.deleteSubtask(subtask);
            server.deleteTask(task);
            exit();
        }
    }

    /**
     * Adds a subtask to the current task
     */
    public void addSubtask() {
        if (task == null) {
            ErrorUtils.alertError("No task to add subtask to!");
            return;
        }
        mainCtrl.showAddSubtask(task);
    }

}
