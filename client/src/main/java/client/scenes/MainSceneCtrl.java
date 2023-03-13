package client.scenes;


import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class MainSceneCtrl {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;

    ObservableList<TaskList> listData;

    ObservableList<Task> taskData;

    @FXML
    ListView boards;

    @FXML
    ListView<TaskList> lists;

    @FXML
    ListView<Task> tasks;

    /**
     * constructor
     * @param mainCtrl the main controller
     */
    @Inject
    public MainSceneCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * initialize the scene with the listview elements as the TaskList scene
     */
    public void initialize() {
        listData = FXCollections.observableArrayList();
        lists.setFixedCellSize(0);
        lists.setItems(listData);
        lists.setCellFactory(new TaskListCtrl(server, this));
        refresh();
    }

    /**
     * go back to the connect screen
     */
    public void back() {
        mainCtrl.showConnect();
    }

    /**
     * refresh the list
     */
    public void refresh() {
        listData = FXCollections.observableList(server.getTaskList());
        taskData = FXCollections.observableList(server.getTasks());
        lists.setItems(listData);
        tasks.setItems(taskData);
    }

    private int i = 0;

    /**
     * add a board to the list
     */
    public void addBoard() {
        boards.getItems().add("Board: " + i);
        i++;
    }

    /**
     * add a list to the list
     */
    public void addList() {
        mainCtrl.showAddList();
    }

    /**
     * add a task to the list
     */
    public void addTask() {
        mainCtrl.showAddTask();
    }

}

