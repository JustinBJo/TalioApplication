package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConnectScreenCtrl {

    private final MainCtrlTalio mainCtrl;

    @FXML
    private TextField address;

    @FXML
    private Label notification;

    /**
     * constructor
     * @param mainCtrl the main controller
     */
    @Inject
    public ConnectScreenCtrl(MainCtrlTalio mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * connect to the server and show the main screen
     */
    public void click() {
        try {
            ServerUtils.setServer(address.getText());
        }
        catch (Exception e) {
            notification.setText(e.getMessage());
        }
        notification.setText("");
        mainCtrl.showMain();
        address.setText("");

    }

}
