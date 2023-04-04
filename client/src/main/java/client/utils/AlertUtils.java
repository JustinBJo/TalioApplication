package client.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

import java.util.Optional;

public class AlertUtils {
    /**
     * Creates an error alert with given text
     * @param text text shown in error alert
     */
    public void alertError(String text) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * Displays an alert which asks for the confirmation of deletion
     * @param type the string which contains the name of
     *             the type of the entity that the user is trying to delete
     * @return true if confirm is clicked, false otherwise
     */
    public boolean confirmDeletion(String type) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete " + type + "?");
        alert.setHeaderText("Delete " + type);
        alert.setContentText("Are you sure you want to delete this "
                + type + "?");

        ButtonType confirmButton = new ButtonType("Confirm");
        ButtonType cancelButton = new ButtonType("Cancel");

        // Remove the default buttons and add Confirm and Cancel buttons
        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == confirmButton) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * confirmation message for leaving admin mode
     * @return whether it has been confirmed or not
     */
    public boolean confirmRevertAdmin() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Leave admin mode?");
        alert.setHeaderText("Leave admin mode");
        alert.setContentText("Are you sure you want to leave the admin " +
                "mode? \n You will need to input the password again " +
                "to re-join!");
        ButtonType confirmButton = new ButtonType("Confirm");
        ButtonType cancelButton = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(confirmButton, cancelButton);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == confirmButton) {
            return true;
        }
        else {
            return false;
        }
    }
}
