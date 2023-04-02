package client.scenes;

import client.utils.ErrorUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditTaskCtrl {
    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;

    private Task editedTask;

    @FXML
    private TextField newTitle;
    @FXML
    private TextField newDescription;
    private String currentTitle;
    private String currentDescription;


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

    /**
     * @param editedTask task which will be edited
     */
    public void setEditedTask(Task editedTask) {
        if (editedTask == null) return;

        this.editedTask = editedTask;

        currentTitle = editedTask.getTitle();
        currentDescription = editedTask.getDescription();
        newTitle.setText(editedTask.getTitle());
        newDescription.setText(editedTask.getDescription());
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

        if (newTitle.getText().isEmpty()) {
            ErrorUtils.alertError("Tasks can't have empty titles!");
            cancel();
        }

        try {
            String newTitleString = newTitle.getText();
            String newDescriptionString = newDescription.getText();

            if (currentTitle.equals(newTitleString)
                    && currentDescription.equals(newDescriptionString)) {
                cancel();
                return;
            }

            if (!currentTitle.equals(newTitleString)) {
                editedTask.setTitle(newTitleString);
                server.updateTaskTitle(editedTask, newTitleString);
            }

            if (!currentDescription.equals(newDescriptionString)) {
                editedTask.setTitle(newDescriptionString);
                server.updateTaskDescription(editedTask, newDescriptionString);
            }

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
