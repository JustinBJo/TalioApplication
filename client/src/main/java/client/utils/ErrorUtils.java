package client.utils;

import javafx.scene.control.Alert;
import javafx.stage.Modality;

public class ErrorUtils {
    /**
     * Creates an error alert with given text
     * @param text text shown in error alert
     */
    public static void alertError(String text) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
