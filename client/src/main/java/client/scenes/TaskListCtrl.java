package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TaskListCtrl {

    private final ServerUtils server;
    private final MainSceneCtrl mainSceneCtrl;
    private final MainCtrlTalio mainCtrl;
    private final RenameCtrl renameCtrl;

    private TaskList taskList;

    private static ServerUtils serverCopy;
    private static MainSceneCtrl mainSceneCtrlCopy;
    private static MainCtrlTalio mainCtrlTalioCopy;
    private static RenameCtrl renameCtrlCopy;

    @FXML
    AnchorPane root;

    @FXML
    Label title;
    @FXML
    ListView<Task> tasks;
    @FXML
    Button deleteList;
    @FXML
    Button addTask;
    @FXML
    Button rename;

    ObservableList<Task> taskData;

    /**
     * Default constructor for TaskListCtrl
     */

    public TaskListCtrl() {
        if (serverCopy != null) {
            this.server = serverCopy;
            this.mainSceneCtrl = mainSceneCtrlCopy;
            this.mainCtrl = mainCtrlTalioCopy;
            this.renameCtrl = renameCtrlCopy;
        }
        else {
            this.server = null;
            this.mainCtrl = null;
            this.mainSceneCtrl = null;
            this.renameCtrl = null; }
    }

    /**
     * Main Constructor for TaskListCtrl
     * @param server the server to fetch the data from
     * @param mainSceneCtrl the board scene that the TaskList belongs to
     */
    @Inject
    public TaskListCtrl(ServerUtils server,
                        MainSceneCtrl mainSceneCtrl, MainCtrlTalio mainCtrl,
                        RenameCtrl renameCtrl) {
        this.server = server;
        this.mainSceneCtrl = mainSceneCtrl;
        this.mainCtrl = mainCtrl;
        this.renameCtrl = renameCtrl;

        serverCopy = server;
        mainSceneCtrlCopy = mainSceneCtrl;
        mainCtrlTalioCopy = mainCtrl;
        renameCtrlCopy = renameCtrl;

        FXMLLoader fxmlLoader = new FXMLLoader((getClass()
                .getResource("TaskList.fxml")));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialise the scene
     */
    public void initialize() {
        taskData = FXCollections.observableArrayList();
        tasks.setItems(taskData);
        tasks.setCellFactory(new CardCtrl(
                server, this, mainSceneCtrl, mainCtrl));
        tasks.setFixedCellSize(0);
        refresh();
    }

    /**
     * Refresh the tasklist
     */
    public void refresh() {
        taskData = FXCollections.observableList(server.getTasks());
        tasks.setItems(taskData);
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

        if (taskList.getTasks() == null) {
            return;
        }
        tasks.getItems().addAll(taskList.getTasks());
    }

    /**
     * used to delete a tasklist from the main scene
     */
    public void delete() {
        TaskList copy = taskList;
        mainCtrl.mainSceneCtrl.lists.getItems().remove(copy);
        mainCtrl.mainSceneCtrl.listData.remove(copy);
        server.deleteTaskList(copy);


    }

    /**
     * Switches to the rename scene and refreshes main scene
     */
    public void rename() {
        mainCtrl.setCurrentTaskList(taskList);
        mainCtrl.showRenameList();
        mainCtrl.mainSceneCtrl.refresh();

    }
}