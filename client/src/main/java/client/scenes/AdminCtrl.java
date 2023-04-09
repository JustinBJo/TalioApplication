package client.scenes;

import client.utils.AlertUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class AdminCtrl {

    private final AlertUtils alertUtils;
    private final MainCtrlTalio mainCtrl;
    private final String PWD = "admin1234";

    @FXML
    Button confirm;
    @FXML
    Button cancel;
    @FXML
    TextField password;

    /**
     * Injector constructor for this class
     *
     * @param mainCtrl   injected main controller
     */
    @Inject
    public AdminCtrl(AlertUtils alertUtils, MainCtrlTalio mainCtrl) {
        this.alertUtils = alertUtils;
        this.mainCtrl = mainCtrl;
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
        if (p.equals(PWD)) {
            mainCtrl.setAdmin(true);
            password.setText("");
            mainCtrl.showAdminBoards();
            mainCtrl.showMain();
        }
        else
            alertUtils.alertError("Wrong password!");
    }
}
