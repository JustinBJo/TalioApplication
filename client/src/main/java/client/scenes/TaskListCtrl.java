package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TaskListCtrl {
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

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;
    private TaskList taskList;

    @Inject
    public TaskListCtrl(ServerUtils server,
                        MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Set the TaskList instance that this Scene holds
     * @param taskList the TaskList instance to be set
     */
    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
        if (taskList.getTitle() == null) {
            taskList.setTitle("Untitled");
        }
        title.setText(taskList.getTitle());

//        if (taskList.getTasks() == null) {
//            return;
//        }
        //tasks.getItems().addAll(taskList.getTasks());
    }

    /**
     * Deletes this task list
     */
    public void delete() {
        server.deleteTaskList(taskList);
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
        // TODO
//        mainCtrl.showAddTask();
    }

    /**
     * Edit a task
     * @throws IOException
     */
    public void editTask() throws IOException {
        // TODO
//        Task currentTask = tasks.getSelectionModel().getSelectedItem();
//        mainCtrl.showEditTask(currentTask);
    }
}
