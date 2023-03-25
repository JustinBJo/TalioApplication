package client.scenes;

import client.utils.ServerUtils;
import commons.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import javax.inject.Inject;
import java.io.IOException;

public class CardCtrl implements IEntityRepresentation<Task> {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;

    private Task task;

    @FXML
    AnchorPane root;
    @FXML
    Label title;
    @FXML
    Button delete;


    /**
     * Main constructor for CardCtrl
     * @param server the server of the application
     * @param mainCtrlTalio main controller of the application
     */
    @Inject
    public CardCtrl(ServerUtils server,
                    MainCtrlTalio mainCtrlTalio) {
        this.server = server;
        this.mainCtrl = mainCtrlTalio;
    }

    /**
     * Set the Task entity that this controller holds
     * @param task the task that is being saved to this controller
     */
    public void setEntity(Task task) {
        this.task = task;
        if (task.getTitle() == null) {
            task.setTitle("Untitled");
        }
        title.setText(task.getTitle());
    }

    /**
     * Used to delete a task from a list
     */
    public void deleteTask() {
        server.deleteTask(task);
        mainCtrl.mainSceneCtrl.refresh();
    }

    /**
     * Edit a task
     * @throws IOException -
     */
    public void editTask() throws IOException {
        mainCtrl.showEditTask(task);
    }
}
