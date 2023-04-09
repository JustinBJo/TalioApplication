package client.utils;

import client.scenes.IEntityRepresentation;
import client.scenes.MainCtrlTalio;
import commons.Task;
import commons.TaskList;
import javax.inject.Inject;
import java.util.List;

public class CardService implements IEntityRepresentation<Task> {
    private final WebsocketUtils websocket;
    private final MainCtrlTalio mainCtrl;
    private final AlertUtils alert;
    private final ServerUtils server;
    private Task task;
    private TaskList parentList;
    private final EntityWebsocketManager<Task> entityWebsocket;

    /**
     * Main constructor for CardCtrl
     *
     * @param mainCtrlTalio main controller of the application
     */
    @Inject
    public CardService(MainCtrlTalio mainCtrlTalio,
                    AlertUtils alert,
                    WebsocketUtils websocket, ServerUtils server) {
        this.mainCtrl = mainCtrlTalio;
        this.alert = alert;
        this.websocket = websocket;
        this.server = server;
        this.entityWebsocket = new EntityWebsocketManager<>(
                websocket,
                "task",
                Task.class,
                this::setEntity
        );
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
        entityWebsocket.register(task.getId(), "updateTitle");
        entityWebsocket.register(task.getId(), "updateDescription");
    }

    /**
     * @param taskList list that holds this task
     */
    public void setParentList(TaskList taskList) {
        this.parentList = taskList;
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

}
