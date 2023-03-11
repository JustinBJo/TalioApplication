package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class AddTaskCtrl {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;

    @FXML
    private TextField title;

    @FXML
    private TextField description;

    /**
     * Constructor for the addTask Controller
     * @param server inject a server instance
     * @param mainCtrl inject a mainCtrl instance
     */
    @Inject
    public AddTaskCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    /**
     * cancel method for the addTask scene
     * returns to the main scene and clears the textFields
     */
    public void cancel() {
        clearFields();
        mainCtrl.mainSceneCtrl.refresh();
        mainCtrl.showMain();
    }

    /**
     * Confirm method for the addtask scene
     * adds the inserted task to the database and
     * returns to the main scene
     */
    public void confirm() {
        try {
            server.addTask(getTask());
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.mainSceneCtrl.refresh();
        mainCtrl.showMain();
    }

    /**
     * returns the current task as a Task element
     * @return current task
     */
    private Task getTask() {
        var t = title.getText();
        var d = description.getText();
        return new Task(t, d, null, null);
    }

    /**
     * auxiliary method
     * clears all textFields
     */
    private void clearFields() {
        title.clear();
        description.clear();
    }

}