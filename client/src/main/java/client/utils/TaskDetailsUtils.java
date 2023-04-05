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

    public void initialize(VBox subtaskContainer) {
        this.subtaskChildrenManager =
                new ChildrenManager<>(
                        subtaskContainer,
                        SubtaskCtrl.class,
                        "Subtask.fxml"
                );
    }

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

    public void setTask(Task task) {
        if (task == null) return;

        this.task = task;
    }

    public void exit() {
        mainCtrl.showMain();
    }

    public void editTask() {
        if (task == null) {
            ErrorUtils.alertError("No task to edit!");
            exit();
        }
        mainCtrl.showEditTask(task);
    }

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

    public void addSubtask() {
        if (task == null) {
            ErrorUtils.alertError("No task to add subtask to!");
            return;
        }
        mainCtrl.showAddSubtask(task);
    }

    public ChildrenManager<Subtask, SubtaskCtrl> getSubtaskChildrenManager() {
        return subtaskChildrenManager;
    }

    public Task getTask() {
        return task;
    }

    public void setSubtaskChildrenManager(ChildrenManager<Subtask, SubtaskCtrl> subtaskChildrenManager) {
        this.subtaskChildrenManager = subtaskChildrenManager;
    }
}
