package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.TaskList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class AddListCtrl {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;

    @FXML
    Button cancel;
    @FXML
    Button confirm;
    @FXML
    TextField textField;

    @Inject
    public AddListCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void pressCancel() {
        textField.clear();
        mainCtrl.mainSceneCtrl.refresh();
        mainCtrl.showMain();
    }

    public void pressConfirm() {
        String title = textField.getText();

        if (!title.isEmpty()) {
            try {
                TaskList taskList = new TaskList(title);
                server.addTaskList(taskList);
                mainCtrl.mainSceneCtrl.lists.getItems().add(title);
            } catch (WebApplicationException e) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }

            textField.clear();
            mainCtrl.mainSceneCtrl.refresh();
            mainCtrl.showMain();
        }
    }
}
