package client.scenes;

import client.utils.ErrorUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class AdminCtrl {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;
    private final String pass = "admin1234";

    @FXML
    Button confirm;
    @FXML
    Button cancel;
    @FXML
    TextField password;

    /**
     * Injector constructor for this class
     * @param server injected server instance
     * @param mainCtrl injected main controller
     */
    @Inject
    public AdminCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * functionality for the cancel button
     */
    public void cancel() {
        password.setText("");
        mainCtrl.showMain();
    }

    /**
     * sets the functionality for the confirm button
     */
    public void confirm() {
        String p = password.getText();
        if (p.equals(pass)) {
            mainCtrl.setAdmin(true);
            password.setText("");
            mainCtrl.showMain();
        }
        else
            ErrorUtils.alertError("Wrong password!");
    }




}
