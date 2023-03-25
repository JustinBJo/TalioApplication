package client.scenes;

import client.utils.ErrorUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditTaskCtrl {
    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;

    private Task editedTask;

    @FXML
    private TextField newTitle;
    @FXML
    private TextField newDescription;
    @FXML
    private Label currentTitle;
    @FXML
    private Label currentDescription;


    /**
     * Constructor for the EditTask
     * @param server injects a server object
     * @param mainCtrl injects a mainCtrl object
     */
    @Inject
    public EditTaskCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setEditedTask(Task editedTask) {
        if (editedTask == null) return;

        this.editedTask = editedTask;

        currentTitle.setText(editedTask.getTitle());
        currentDescription.setText(editedTask.getDescription());
    }

    /**
     * Method cancel for cancelling the editing of a task
     * returns to main scene
     */
    public void cancel() {
        mainCtrl.showMain();
    }

    /**
     * Saving the changes the user made to
     * task title and description
     */
    public void saveChanges() {
        if (editedTask == null) {
            ErrorUtils.alertError("No task to edit!");
            cancel();
        }

        try {
            String newTitleString = newTitle.getText();
            String newDescriptionString = newDescription.getText();

            if (newTitleString.length() == 0
                    && newDescriptionString.length() == 0) {
                cancel();
                return;
            }

            if (newTitleString.length() >= 1)
                server.updateTaskTitle(editedTask, newTitleString);
            if (newDescriptionString.length() >= 1)
                server.updateTaskDescription(editedTask, newDescriptionString);

        }
        catch (WebApplicationException e) {
            ErrorUtils.alertError(e.getMessage());
            return;
        }

        newTitle.clear();
        newDescription.clear();
        mainCtrl.showMain();
    }
}
