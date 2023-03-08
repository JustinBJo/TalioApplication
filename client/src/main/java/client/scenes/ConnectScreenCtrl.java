package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ConnectScreenCtrl {

    private final MainCtrlTalio mainCtrl;

    @FXML
    private TextField address;

    @Inject
    public ConnectScreenCtrl(MainCtrlTalio mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void click() {
        mainCtrl.showMain();

    }

}
