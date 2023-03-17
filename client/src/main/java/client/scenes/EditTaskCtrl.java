package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditTaskCtrl {
    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;

    private Task task;

    @FXML
    private TextField newTitle;

    @FXML
    private TextField newDescription;

    @FXML
    private Label currentTitle;

    @FXML
    private Label currentDescription;



    /**
     * Constructor for the EditTask
     * @param server injects a server object
     * @param mainCtrl injects a mainCtrl object
     */
    @Inject
    public EditTaskCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    /**
     * Method cancel for cancelling the editing of a task
     * returns to main scene
     */
    public void cancel() {
        mainCtrl.mainSceneCtrl.refresh();
        mainCtrl.showMain();
    }
}
