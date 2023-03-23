package client.scenes;

import client.utils.ServerUtils;
import commons.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import javax.inject.Inject;
import java.io.IOException;

public class CardCtrl implements Callback<ListView<Task>, ListCell<Task>> {

    private final ServerUtils server;
    private final TaskListCtrl taskListCtrl;
    private final MainSceneCtrl mainSceneCtrl;
    private final MainCtrlTalio mainCtrl;

    private Task task;

    private static ServerUtils serverCopy;
    private static TaskListCtrl taskListCtrlCopy;
    private static MainSceneCtrl mainSceneCtrlCopy;
    private static MainCtrlTalio mainCtrlTalioCopy;

    @FXML
    AnchorPane root;
    @FXML
    Label title;
    @FXML
    Button delete;

    /**
     * Default constructor for CardCtrl
     */
    public CardCtrl() {
        if (serverCopy != null) {
            this.server = serverCopy;
            this.taskListCtrl = taskListCtrlCopy;
            this.mainSceneCtrl = mainSceneCtrlCopy;
            this.mainCtrl = mainCtrlTalioCopy;
        }
        else {
            this.server = null;
            this.taskListCtrl = null;
            this.mainCtrl = null;
            this.mainSceneCtrl = null;
        }
    }

    /**
     * Main constructor for CardCtrl
     * @param server the server of the application
     * @param taskListCtrl controller for the parent tasklist
     * @param mainSceneCtrl controller for the parent board
     * @param mainCtrlTalio main controller of the application
     */
    @Inject
    public CardCtrl(ServerUtils server,
                    TaskListCtrl taskListCtrl,
                    MainSceneCtrl mainSceneCtrl,
                    MainCtrlTalio mainCtrlTalio) {
        this.server = server;
        this.taskListCtrl = taskListCtrl;
        this.mainCtrl = mainCtrlTalio;
        this.mainSceneCtrl = mainSceneCtrl;

        this.serverCopy = server;
        this.taskListCtrlCopy = taskListCtrl;
        this.mainCtrlTalioCopy = mainCtrlTalio;
        this.mainSceneCtrlCopy = mainSceneCtrl;
    }

    /**
     * Set the Task entity that this controller holds
     * @param task the task that is being saved to this controller
     */
    public void setTask(Task task) {
        this.task = task;
        if (task.getTitle() == null) {
            task.setTitle("Untitled");
        }
        title.setText(task.getTitle());
    }

    /**
     *
     * @param param The single argument upon which the returned value should be
     *      determined.
     * @return
     */
    @Override
    public ListCell<Task> call(ListView<Task> param) {
        return new ListCell<Task>() {
            private CardCtrl controller;
            private FXMLLoader loader;

            {
                loader = new FXMLLoader(getClass()
                        .getResource("Card.fxml"));
                try {
                    loader.load();
                    controller = loader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    // Clear the cell content if there is no item to display
                    setText(null);
                    setGraphic(null);
                } else {
                    controller.setTask(task);
                    setGraphic(controller.root);
                }
            }
        };
    }

    /**
     * Used to delete a task from a list
     */
    public void deleteTask() {
        taskListCtrl.tasks.getItems().remove(task);
        server.deleteTask(task);
        mainCtrl.mainSceneCtrl.refresh();
    }

    /**
     * View the details of a task after clicking twice on the card
     */
    public void viewTask() {
        Task currentTask = task;
        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        System.out.println("Double clicked");
                        try {
                            mainCtrl.showTaskDetails(currentTask);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

}
