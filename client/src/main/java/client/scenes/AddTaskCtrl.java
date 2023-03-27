package client.scenes;

import client.utils.ErrorUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddTaskCtrl {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;

    private TaskList parentTaskList;

    @FXML
    private TextField title;
    @FXML
    private TextField description;


    /**
     * Constructor for the Addtask
     * @param server injects a server object
     * @param mainCtrl injects a mainCtrl object
     */
    @Inject
    public AddTaskCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
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
            server.addTask(getTask(), parentTaskList);
        } catch (WebApplicationException e) {
            ErrorUtils.alertError(e.getMessage());
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