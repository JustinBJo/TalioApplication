package client.scenes;


import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.TaskList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class MainSceneCtrl {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;

    ObservableList<TaskList> listData;

    @FXML
    ListView boards;

    @FXML
    ListView<TaskList> lists;

    /**
     * constructor
     * @param mainCtrl the main controller
     */
    @Inject
    public MainSceneCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize() {
        listData = FXCollections.observableArrayList();
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
        lists.setItems(listData);
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


}
