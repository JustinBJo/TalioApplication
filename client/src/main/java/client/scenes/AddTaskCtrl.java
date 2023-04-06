package client.scenes;

import client.utils.AddTaskService;
import client.utils.ServerUtils;
import client.utils.AlertUtils;
import client.utils.WebsocketUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddTaskCtrl {

    private final AddTaskService service;
    private final WebsocketUtils websocket;
    private final AlertUtils alertUtils;

    private TaskList parentTaskList;

    @FXML
    private TextField title;
    @FXML
    private TextField description;

    /**
     * Constructor for the AddTaskCtrl
     * @param service the AddTaskCtrl service
     */
    @Inject
    public AddTaskCtrl(AddTaskService service,
                        AlertUtils alertUtils,
                        WebsocketUtils websocket) {
        this.service = service;
        this.alertUtils = alertUtils;
        this.websocket = websocket;
    }

    /**
     * @param parentTaskList task list to which the new task will be added
     */
    public void setParentTaskList(TaskList parentTaskList) {
        service.setParentTaskList(parentTaskList);
    }

    /**
     * @return the parent task list
     */
    public TaskList getParentTaskList() {
        return service.getParentTaskList();
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

    /**
     * setter for the server utils
     * @param server the server utils
     */
    public void setServer(ServerUtils server) {
        service.setServer(server);
    }

    /**
     * getter for the server utils
     * @return the server utils
     */
    public ServerUtils getServer() {
        return service.getServer();
    }

    /**
     * Method cancel for cancelling the insertion of a new task
     * returns to main scene
     */
    public void cancel() {
        clearFields();
        service.getMainCtrl().showMain();
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
        service.setParentTaskList(null);
        service.getMainCtrl().showMain();
    }

}
