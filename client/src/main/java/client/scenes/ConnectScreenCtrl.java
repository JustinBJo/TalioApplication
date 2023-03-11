package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConnectScreenCtrl {

    private final MainCtrlTalio mainCtrl;

    private final ServerUtils server;

    @FXML
    private TextField address;

    /**
     * constructor
     * @param mainCtrl the main controller
     */
    @FXML
    private Label notification;

    /**
     * constructor
     * @param mainCtrl the main controller
     * @param server the server utils
     */
    @Inject
    public ConnectScreenCtrl(MainCtrlTalio mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * connect to the server and show the main screen
     */
    public void click() {

        if (!ServerUtils.setServer(address.getText())) {
            notification.setText("connection failed");
        }

        else {
            notification.setText("");
            mainCtrl.showMain();
        }
        address.setText("");

    }

}
