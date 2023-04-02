package client.scenes;

import client.utils.AlertUtils;
import client.utils.WebsocketUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddTaskCtrl {

    private final WebsocketUtils websocket;
    private final MainCtrlTalio mainCtrl;
    private final AlertUtils alertUtils;

    private TaskList parentTaskList;

    @FXML
    private TextField title;
    @FXML
    private TextField description;


    /**
     * Constructor for the Addtask
     * @param mainCtrl injects a mainCtrl object
     */
    @Inject
    public AddTaskCtrl(WebsocketUtils websocket,
                       MainCtrlTalio mainCtrl,
                       AlertUtils alertUtils) {
        this.alertUtils = alertUtils;
        this.mainCtrl = mainCtrl;
        this.websocket = websocket;
    }

    /**
     * @param parentTaskList task list to which the new task will be added
     */
    public void setParentTaskList(TaskList parentTaskList) {
        this.parentTaskList = parentTaskList;
    }

    /**
     * Method cancel for cancelling the insertion of a new task
     * returns to main scene
     */
    public void cancel() {
        clearFields();
        mainCtrl.showMain();
    }

    /**
     * Method confirm adds the inserted task to the database,
     * returns to the main scene and refreshes it in order for
     * the new task to be displayed
     * Throws error in case of exception
     *
     */
    public void confirm() {
        try {
            websocket.addTask(getTask(), parentTaskList);
        } catch (WebApplicationException e) {
            alertUtils.alertError(e.getMessage());
            return;
        }

        clearFields();
        parentTaskList = null;
        mainCtrl.showMain();
    }

    /**
     * getTask provides the user with the current task
     * @return the task as a Task object
     */
    private Task getTask() {
        var t = title.getText();
        var d = description.getText();
        return new Task(t, d, null, null);
    }

    /**
     * Auxiliary method, clears all text fields
     */
    private void clearFields() {
        title.clear();
        description.clear();
    }

}