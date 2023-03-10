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

    private ObservableList taskLists;

    @FXML
    ListView boards;
    @FXML
    ListView<TaskList> lists;

    /**
     * constructor
     * @param mainCtrl the main controller
     */
    @Inject
    public  MainSceneCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
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
        taskLists = FXCollections.observableList(server.getTaskList());
        lists.setItems(taskLists);
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
