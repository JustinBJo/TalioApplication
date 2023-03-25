package client.scenes;

import client.utils.ChildrenManager;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class TaskListCtrl implements IEntityRepresentation<TaskList> {
    @FXML
    AnchorPane root;
    @FXML
    Label title;
    @FXML
    Button deleteList;
    @FXML
    Button addTask;
    @FXML
    Button rename;
    @FXML
    VBox taskContainer;

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;
    private ChildrenManager<Task, CardCtrl> taskChildrenManager;
    private TaskList taskList;

    @Inject
    public TaskListCtrl(ServerUtils server,
                        MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }


    /**
     * Create children manager after FXML components are initialized
     */
    public void initialize() {
        this.taskChildrenManager =
                new ChildrenManager<>(
                    taskContainer,
                    CardCtrl.class,
                    "Card.fxml"
                );
    }

    /**
     * Set the TaskList instance that this Scene holds
     * @param taskList the TaskList instance to be set
     */
    public void setEntity(TaskList taskList) {
        this.taskList = taskList;
        if (taskList.getTitle() == null) {
            taskList.setTitle("Untitled");
        }
        title.setText(taskList.getTitle());

        refresh();
    }

    public void refresh() {
        if (taskList == null) { taskChildrenManager.updateChildren(new ArrayList<>()); }
        var tasks = server.getTaskListData(taskList);
        taskChildrenManager.updateChildren(tasks);
    }

    /**
     * Deletes this task list
     */
    public void delete() {
        server.deleteTaskList(taskList);
        taskList = null;
        mainCtrl.mainSceneCtrl.refresh();
    }

    /**
     * Switches to the rename scene and refreshes main scene
     */
    public void rename() {
        mainCtrl.setCurrentTaskList(taskList);
        mainCtrl.showRenameList();
        mainCtrl.mainSceneCtrl.refresh();
    }


    /**
     * add a task to the list
     */
    public void addTask() {
        if (taskList == null) {
            return; // TODO alert error
        }
        mainCtrl.showAddTask(taskList);
    }

    /**
     * Edit a task
     * @throws IOException
     */
    public void editTask() throws IOException {
        // TODO
//        Task selectedTask = taskContainer.getSelectionModel().getSelectedItem();
//        mainCtrl.showEditTask(selectedTask);
    }
}
