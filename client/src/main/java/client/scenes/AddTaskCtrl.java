package client.scenes;

import client.utils.AddTaskService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddTaskCtrl {
    private final AddTaskService service;
    @FXML
    private TextField title;
    @FXML
    private TextField description;

    /**
     * Constructor for the AddTaskCtrl
     */
    @Inject
    public AddTaskCtrl(AddTaskService service) {
        this.service = service;
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

}