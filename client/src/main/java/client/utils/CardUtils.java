package client.utils;

import client.scenes.IEntityRepresentation;
import commons.Task;
import commons.TaskList;
import javax.inject.Inject;
import java.util.List;

public class CardUtils implements IEntityRepresentation<Task> {
    private WebsocketUtils websocket;
    private MainCtrlTalio mainCtrl;
    private AlertUtils alert;
    private ServerUtils server;
    private Task task;
    private TaskList parentList;

    /**
     * Main constructor for CardCtrl
     *
     * @param mainCtrlTalio main controller of the application
     */
    @Inject
    public CardUtils(MainCtrlTalio mainCtrlTalio,
                       AlertUtils alert,
                       WebsocketUtils websocket, ServerUtils server) {
        this.mainCtrl = mainCtrlTalio;
        this.alert = alert;
        this.websocket = websocket;
        this.server = server;
    }

    /**
     * Set the Task entity that this controller holds
     * @param task the task that is being saved to this controller
     */
    public void setEntity(Task task) {
        this.task = task;
        if (task.getTitle() == null) {
            task.setTitle("Untitled");
            return;
        }
    }

    /**
     * @param taskList list that holds this task
     */
    public void setParentList(TaskList taskList) {
        this.parentList = taskList;
    }

    /**
     * @return the parent list
     */
    public TaskList getParentList() {
        return parentList;
    }

    /**
     * Show edit task scene for this task
     */
    public void editTask() {
        mainCtrl.showEditTask(this.task);
    }

    /**
     * Used to delete a task from a list
     */
    public void deleteTask() {
        boolean confirmation = alert.confirmDeletion("task");

        // Check the user's response and perform the desired action
        if (confirmation) {
            websocket.deleteTask(task);
        }
    }

    /**
     * Used to move a task up in the parent list
     */
    public void moveUp() {
        TaskList currentTaskList = parentList;

        List<Task> currentTasks = server
                .getTaskListData(currentTaskList);
        int taskIndex = currentTasks.indexOf(task);
        taskIndex--;
        if (taskIndex < 0 || taskIndex >= currentTasks.size()) {
            alert.alertError("You cannot move the task higher.");
            return;
        }
        currentTasks.remove(task);
        currentTasks.add(taskIndex, task);
        currentTaskList.setTasks(currentTasks);
        websocket.updateTaskListChildren(currentTaskList, currentTasks);
    }

    /**
     * Used to move a task down in the parent list
     */
    public void moveDown() {
        TaskList currentTaskList = parentList;

        if (currentTaskList == null) {
            System.out.println("The task is not part of a list");
            return;
        }
        List<Task> currentTasks = server
                .getTaskListData(currentTaskList);
        int taskIndex = currentTasks.indexOf(task);
        taskIndex++;
        if (taskIndex >= currentTasks.size()) {
            alert.alertError("You cannot move the task lower.");
            return;
        }
        currentTasks.remove(task);
        currentTasks.add(taskIndex, task);
        currentTaskList.setTasks(currentTasks);
        websocket.updateTaskListChildren(currentTaskList, currentTasks);
    }

    /**
     * Getter for the task
     * @return the current task
     */
    public Task getTask() {
        return task;
    }

    /**
     * Getter for the server
     * @return the server
     */
    public ServerUtils getServer() {
        return server;
    }

    /**
     * Getter for the websocket
     * @return the websocket
     */
    public WebsocketUtils getWebsocket() {
        return websocket;
    }

    /**
     * Getter for the mainCtrl
     * @return the mainCtrl
     */
    public MainCtrlTalio getMainCtrl() {
        return mainCtrl;
    }

    /**
     * Getter for the alert
     * @return the alert
     */
    public AlertUtils getAlert() {
        return alert;
    }

    /**
     * Setter for the websocket
     * @param websocket the websocket
     */
    public void setWebsocket(WebsocketUtils websocket) {
        this.websocket = websocket;
    }

    /**
     * Setter for the mainCtrl
     * @param mainCtrl the mainCtrl
     */
    public void setMainCtrl(MainCtrlTalio mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Setter for the alert
     * @param alert the alert
     */
    public void setAlert(AlertUtils alert) {
        this.alert = alert;
    }

    /**
     * Setter for the server
     * @param server the server
     */
    public void setServer(ServerUtils server) {
        this.server = server;
    }

    /**
     * Setter for the task
     * @param task the task
     */
    public void setTask(Task task) {
        this.task = task;
    }
}
