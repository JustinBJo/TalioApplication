package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ConnectScreenCtrl {

    private final MainCtrlTalio mainCtrl;

    @FXML
    private TextField address;

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
        mainCtrl.showMain();
        mainCtrl.mainSceneCtrl.validateUser();

    }

}
