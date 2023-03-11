package client.scenes;

import client.utils.ServerUtils;
import commons.Task;
import commons.TaskList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;

public class TaskListCtrl
        implements Callback<ListView<TaskList>, ListCell<TaskList>> {

    private final ServerUtils server;
    private final MainSceneCtrl mainSceneCtrl;

    private TaskList taskList;

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

    private ObservableList<Task> taskView;

    /**
     * Default constructor for TaskListCtrl
     */
    public TaskListCtrl() {
        this.server = null;
        this.mainSceneCtrl = null;
    }

    /**
     * Main Constructor for TaskListCtrl
     * @param server the server to fetch the data from
     * @param mainSceneCtrl the board scene that the TaskList belongs to
     */
    public TaskListCtrl(ServerUtils server,
                        MainSceneCtrl mainSceneCtrl) {
        this.server = server;
        this.mainSceneCtrl = mainSceneCtrl;

        FXMLLoader fxmlLoader = new FXMLLoader((getClass()
                .getResource("TaskList.fxml")));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        tasks.getItems().addAll(taskList.getTasks());
    }

    /**
     *
     * @param param The single argument upon which the returned value should be
     *      determined.
     * @return the cells with the TaskList Scene
     */
    @Override
    public ListCell<TaskList> call(ListView<TaskList> param) {
        return new ListCell<TaskList>() {
            private TaskListCtrl controller;
            private FXMLLoader loader;

            {
                loader = new FXMLLoader(getClass()
                        .getResource("TaskList.fxml"));
                try {
                    loader.load();
                    controller = loader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void updateItem(TaskList taskList, boolean empty) {
                super.updateItem(taskList, empty);
                if (empty || taskList == null) {
                    // Clear the cell content if there is no item to display
                    setText(null);
                    setGraphic(null);
                } else {
                    controller.setTaskList(taskList);
                    setGraphic(controller.root);
                }
            }
        };
    }

}
