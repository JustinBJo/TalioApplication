package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditTaskCtrl {
    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;

    private static ServerUtils serverCopy;
    private static MainCtrlTalio mainCtrlTalioCopy;


    private Task task;

    @FXML
    private TextField newTitle;

    @FXML
    private TextField newDescription;

    @FXML
    private Label currentTitle;

    @FXML
    private Label currentDescription;

    public EditTaskCtrl(){
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

        FXMLLoader fxmlLoader = new FXMLLoader((getClass()
                .getResource("EditTask.fxml")));

    }

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

    public void saveChanges(){

    }
}
