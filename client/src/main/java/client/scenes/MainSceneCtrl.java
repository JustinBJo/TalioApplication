package client.scenes;


import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.stream.Collectors;

public class MainSceneCtrl {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;

    private ObservableList taskLists;

    @FXML
    ListView boards;
    @FXML
    ListView<String> lists;

    @FXML
    ListView<String> tasks;

    @Inject
    public  MainSceneCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void back() {
        mainCtrl.showConnect();
    }

    public void refresh() {
        taskLists = FXCollections.observableList(
                server.getTaskList().stream()
                        .map(TaskList::getTitle)
                        .collect(Collectors.toList())
        );
        ObservableList<String> task = FXCollections.observableList(
                server.getTasks().stream()
                        .map(Task::getTitle)
                        .collect(Collectors.toList())
        );
        lists.setItems(taskLists);
        tasks.setItems(task);
    }

    private int i = 0;

    public void addBoard(){

        boards.getItems().add("Board: " + i);
        i++;
        }

    public void addList() {
        mainCtrl.showAddList();
    }

    public void addTask(){mainCtrl.showAddTask();}

}
