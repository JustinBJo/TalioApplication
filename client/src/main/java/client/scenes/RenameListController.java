package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class RenameListController {
    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;


    @FXML
    private TextField newName;

    @FXML
    private Button confirm;

    @FXML
    private Button cancel;


    /**
     * Constructor for the rename list controller
     * @param mainCtrl the main controller
     * @param server the server used
     *
     */
    @Inject
    public RenameListController(MainCtrlTalio mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * method for actioning the cancel button
     * returns to main scene
     */
    public void cancel() {
        newName.clear();
        mainCtrl.mainSceneCtrl.refresh();
        mainCtrl.showMain();
    }

    /**
     * method for the confirm button
     * accesses server method and updates the list
     */
    public void confirm() {
        try {
            String name = newName.getText();
            server.updateTaskList(mainCtrl.getCurrentTaskList(), name);
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        newName.clear();
        mainCtrl.mainSceneCtrl.refresh();
        mainCtrl.showMain();
    }

}
