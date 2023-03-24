package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class EditTaskCtrl {
    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;

    private static ServerUtils serverCopy;
    private static MainCtrlTalio mainCtrlTalioCopy;

    @FXML
    private TextField newTitle;

    @FXML
    private TextField newDescription;

    @FXML
    private Label currentTitle;

    @FXML
    private Label currentDescription;

    /**
     * Empty constructor
     */
    public EditTaskCtrl() {
        if (serverCopy != null) {
            this.server = serverCopy;
            this.mainCtrl = mainCtrlTalioCopy;
        }
        else {
            this.server = null;
            this.mainCtrl = null;
        }

    }

    /**
     * Constructor for the EditTask
     * @param server injects a server object
     * @param mainCtrl injects a mainCtrl object
     */
    @Inject
    public EditTaskCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;

        this.serverCopy = server;
        this.mainCtrlTalioCopy = mainCtrl;
    }

    /**
     * initialize method for EditTask
     */
    @FXML
    public void initialize() {

    }

    /**
     * Method cancel for cancelling the editing of a task
     * returns to main scene
     */
    public void cancel() {
        mainCtrl.mainSceneCtrl.refresh();
        mainCtrl.showMain();
    }

    /**
     * Saving the changes the user made to
     * task title and description
     */
    public void saveChanges() {
        try {
            Task task = mainCtrl.getCurrentTask();
            String newTitleString = newTitle.getText();
            String newDescriptionString = newDescription.getText();

            if (newTitleString.length() == 0
                    && newDescriptionString.length() == 0) {
                cancel();
                return;
            }

            if (newTitleString.length() >= 1)
                server.updateTaskTitle(task, newTitleString);
            if (newDescriptionString.length() >= 1)
                server.updateTaskDescription(task, newDescriptionString);

        }
        catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        newTitle.clear();
        newDescription.clear();
        mainCtrl.mainSceneCtrl.refresh();
        mainCtrl.showMain();
    }
}
