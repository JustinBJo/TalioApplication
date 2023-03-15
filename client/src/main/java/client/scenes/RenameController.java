package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class RenameController {

    enum Type {
        TaskList,
        Board
    }

    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;

    private Type type;

    @FXML
    private Label header;

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
    public RenameController(MainCtrlTalio mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Initializes the scene for a certain titled entity type
     * @param type the type of the entity that is being renamed
     */
    public void initialize(Type type) {
        this.type = type;

        switch (type) {
            case TaskList:
                this.header.setText("Add new list");
                break;
            case Board:
                this.header.setText("Add new board");
                break;
            default:
                newName.clear();
                mainCtrl.mainSceneCtrl.refresh();
                mainCtrl.showMain();
                break;
        }
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
     * accesses server method and updates the entity
     */
    public void confirm() {
        try {
            String name = newName.getText();

            if (name.isEmpty()) {
                return;
            }

            switch (type) {
                case TaskList:
                    server.updateTaskList(mainCtrl.getCurrentTaskList(), name);
                    break;
                case Board:
                    server.updateBoard(mainCtrl.getActiveBoard(), name);
                    break;
                default:
                    alertError("Unexpected error in renaming entity");
                    cancel();
                    break;
            }
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

    /**
     * Creates an error alert with given text
     * Created by @fsiqueiracarne
     * @param text text shown in error alert
     */
    private void alertError(String text) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(text);
        alert.showAndWait();
    }


}
