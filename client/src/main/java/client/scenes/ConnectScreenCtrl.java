package client.scenes;

import client.utils.ConnectScreenUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConnectScreenCtrl {

   private final ConnectScreenUtils utils;

    @FXML
    private TextField address;
    @FXML
    private Label notification;

    /**
     * Constructor for the ConnectScreen controller
     * @param utils the service for logic used
     */
    @Inject
    public ConnectScreenCtrl(ConnectScreenUtils utils) {
        this.utils = utils;
    }

    /**
     * connect to the server and show the main screen
     */
    public void click() {
        try {
            utils.setServer(address.getText());
        }
        catch (Exception e) {
            notification.setText(e.getMessage());
            return;
        }
        notification.setText("");
        utils.changeServer(address.getText(7, address.getText().length()));

        address.setText("");

    }

}
