package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddListCtrl {

    private final MainCtrlTalio mainCtrl;

    @FXML
    Button cancel;
    @FXML
    Button confirm;
    @FXML
    TextField textField;

    @Inject
    public AddListCtrl(MainCtrlTalio mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void pressCancel() {
        mainCtrl.showMain();
    }

    public void pressConfirm() {
        String title = textField.getText();

        if (!title.isEmpty()) {
            MainSceneCtrl mainSceneCtrl = mainCtrl.mainSceneCtrl;
            mainSceneCtrl.lists.getItems().add(title);
            mainCtrl.showMain();
        }
    }
}
