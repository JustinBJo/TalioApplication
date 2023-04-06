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
    @FXML
    private Label notification;

    /**
     * constructor
     * @param mainCtrl the main controller
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
        try {
            server.setServer(address.getText());
        }
        catch (Exception e) {
            notification.setText(e.getMessage());
            return;
        }
        notification.setText("");

        mainCtrl.changeServer(
                // get the address after "http://"
                address.getText(7, address.getText().length())
        );

        mainCtrl.showMain();
        address.setText("");

    }

}
