package client.utils;

import client.scenes.MainCtrlTalio;
import client.scenes.SubtaskCtrl;
import com.google.inject.Inject;
import commons.Subtask;
import commons.Task;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class TaskDetailsUtils {
    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;

    private ChildrenManager<Subtask, SubtaskCtrl> subtaskChildrenManager;

    private Task task;

    /**
     * Constructor for the task details
     * @param server injects a server object
     * @param mainCtrl injects a mainCtrl object
     */
    @Inject
    public TaskDetailsUtils(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * used to initialize the scene
     * @param subtaskContainer the Container for children
     */
    public void initialize(VBox subtaskContainer) {
        this.subtaskChildrenManager =
                new ChildrenManager<>(
                        subtaskContainer,
                        SubtaskCtrl.class,
                        "Subtask.fxml"
                );
    }

    /**
     * logic for refreshing the scene
     */
    public void refresh() {
        if (task == null) {
            // no children if there's no task list
            subtaskChildrenManager.updateChildren(new ArrayList<>());
        }
        var subtasks = server.getTaskData(task);
        subtaskChildrenManager.updateChildren(subtasks);

        for (SubtaskCtrl subtaskCtrl :
                subtaskChildrenManager.getChildrenCtrls()) {
            subtaskCtrl.setParentTask(task);
        }
    }

    /**
     * sets the current task
     * @param task task to be set
     */
    public void setTask(Task task) {
        if (task == null) return;

        this.task = task;
    }

    /**
     * logic for the exit method
     */
    public void exit() {
        mainCtrl.showMain();
    }

    /**
     * logic for the edit Task method
     */
    public void editTask() {
        if (task == null) {
            ErrorUtils.alertError("No task to edit!");
            exit();
        }
        mainCtrl.showEditTask(task);
    }

    /**
     * deletes the current task
     */
    public void deleteTask() {
        if (task == null) {
            ErrorUtils.alertError("No task to delete!");
            exit();
        }

        boolean confirmation = server.confirmDeletion("task");

        // Check the user's response and perform the desired action
        if (confirmation) {
            server.deleteTask(task);
            exit();
        }
    }

    /**
     * logic for adding a new subtask
     */
    public void addSubtask() {
        if (task == null) {
            ErrorUtils.alertError("No task to add subtask to!");
            return;
        }
        mainCtrl.showAddSubtask(task);
    }

    /**
     * getter for the Children manager of the subtask list
     * @return
     */
    public ChildrenManager<Subtask, SubtaskCtrl> getSubtaskChildrenManager() {
        return subtaskChildrenManager;
    }

    /**
     * getter for the current task
     * @return the current task
     */
    public Task getTask() {
        return task;
    }

    /**
     * setter for the ChildrenManager of the subtask
     * @param subtaskChildrenManager the ChildrenManager to be set
     */
    public void setSubtaskChildrenManager(ChildrenManager<Subtask,
            SubtaskCtrl> subtaskChildrenManager) {
        this.subtaskChildrenManager = subtaskChildrenManager;
    }
}
