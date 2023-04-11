package client.scenes;

import client.utils.AlertUtils;
import client.utils.EditTaskService;
import client.utils.WebsocketUtils;
import com.google.inject.Inject;
import commons.Task;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditTaskCtrl {
    private final EditTaskService service;
    private final AlertUtils alertUtils;
    private final WebsocketUtils websocket;
    private Task editedTask;
    @FXML
    private TextField newTitle;
    @FXML
    private TextField newDescription;
    private String currentTitle;
    private String currentDescription;

    /**
     * Constructor for the EditTaskCtrl
     * @param service the edit task service
     */
    @Inject
    public EditTaskCtrl(EditTaskService service,
                        AlertUtils alertUtils,
                        WebsocketUtils websocket) {
        this.alertUtils = alertUtils;
        this.service = service;
        this.websocket = websocket;
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
        service.cancel();
    }

    /**
     * Saving the changes the user made to
     * task title and description
     */
    public void saveChanges() {
        if (editedTask == null) {
            alertUtils.alertError("No task to edit!");
            cancel();
        }

        if (newTitle.getText().isEmpty()) {
            alertUtils.alertError("Tasks can't have empty titles!");
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
                websocket.updateTaskTitle(editedTask, newTitleString);
            }

            if (!currentDescription.equals(newDescriptionString)) {
                editedTask.setDescription(newDescriptionString);
                if (newDescriptionString.isEmpty())
                    newDescriptionString =
                            "HARDCODED-EMPTY-DESCRIPTION-METHOD" +
                                    "-FOR-EDITING-TASKS";
                websocket.updateTaskDescription(
                        editedTask,
                        newDescriptionString
                );
            }

        }
        catch (WebApplicationException e) {
            alertUtils.alertError(e.getMessage());
            return;
        }

        newTitle.clear();
        newDescription.clear();
        exit();
    }

    /**
     * Method that brings you to the main screen
     */
    public void exit() {
        service.getServer().resetTask(editedTask.getId());
        service.getMainCtrl().showMain();
    }

}
