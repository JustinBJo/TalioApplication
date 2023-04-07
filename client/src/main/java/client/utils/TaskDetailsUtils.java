package client.utils;

import client.scenes.MainCtrlTalio;
import client.scenes.SubtaskCtrl;
import com.google.inject.Inject;
import commons.Subtask;
import commons.Task;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailsUtils {
    private final MainCtrlTalio mainCtrl;
    private final WebsocketUtils websocket;
    private final AlertUtils alertUtils;
    private final ServerUtils server;

    private ChildrenManager<Subtask, SubtaskCtrl> subtaskChildrenManager;
    private ParentWebsocketManager<Subtask, SubtaskCtrl> parentWebsocket;
    private EntityWebsocketManager<Task> entityWebsocket;

    private Task task;

    /**
     * Constructor for the TaskDetails service
     * @param websocket the websocket used
     * @param mainCtrl mainCtrl used
     * @param alertUtils alertUtils used
     * @param server server used
     */
    @Inject
    public TaskDetailsUtils(WebsocketUtils websocket,
                           MainCtrlTalio mainCtrl,
                           AlertUtils alertUtils,
                           ServerUtils server) {
        this.alertUtils = alertUtils;
        this.websocket = websocket;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * used to initialize the scene
     * @param subtaskContainer the Container for children
     */
    public void initialize(VBox subtaskContainer) {
        // Set up children manager
        this.subtaskChildrenManager =
                new ChildrenManager<>(
                        subtaskContainer,
                        SubtaskCtrl.class,
                        "Subtask.fxml"
                );
        subtaskChildrenManager.setUpdatedChildConsumer(
                subtaskCtrl -> subtaskCtrl.setParent(task)
        );

        // Set up websockets
        this.parentWebsocket = new ParentWebsocketManager<>(
                websocket,
                "subtask",
                Subtask.class,
                subtaskChildrenManager
        );

        this.entityWebsocket = new EntityWebsocketManager<>(
                websocket,
                "task",
                Task.class,
                this::setEntity
        );
    }


    /**
     * Sets the current task entity
     * @param task the task to be set
     */
    public void setEntity(Task task) {
        if (task == null) return;

        this.task = task;
        if (task.getTitle() == null) {
            task.setTitle("Untitled");
        }

        var subtasks = server.getTaskData(task);
        subtaskChildrenManager.clear();
        subtaskChildrenManager.updateChildren(subtasks);
        for (SubtaskCtrl subtaskCtrl :
                subtaskChildrenManager.getChildrenCtrls()) {
            subtaskCtrl.setParent(task);
        }

        parentWebsocket.register(task.getId());
        entityWebsocket.register(task.getId(), "updateTitle");
        entityWebsocket.register(task.getId(), "updateDescription");
        websocket.registerForMessages(
                "/topic/task/updateChildren/" + task.getId(),
                Task.class,
                (t) -> {
                    subtaskChildrenManager.updateChildren(new ArrayList<>());
                    subtaskChildrenManager.updateChildren(t.getSubtasks());
                }
        );
        setupCloseOnDelete();

    }

    /**
     * exits the task details if the task is deleted
     */
    private void setupCloseOnDelete() {
        websocket.registerForMessages(
                "/topic/task/delete",
                Task.class,
                t -> {
                    if (t.getId().equals(task.getId())) {
                        exit();
                    }
                }
        );
    }


    /**
     * logic for the exit method
     */
    public void exit() {
        mainCtrl.showMain();
    }

    /**
     * logic for the edit Task method
     */
    public void editTask() {
        if (task == null) {
            alertUtils.alertError("No task to edit!");
            exit();
        }
        mainCtrl.showEditTask(task);
    }

    /**
     * deletes the current task
     */
    public void deleteTask() {
        if (task == null) {
            alertUtils.alertError("No task to delete!");
            exit();
        }

        boolean confirmation = alertUtils.confirmDeletion("task");

        // Check the user's response and perform the desired action
        if (confirmation) {
            List<Subtask> subtasks = task.getSubtasks();
            for (Subtask subtask : subtasks)
                websocket.deleteSubtask(subtask);
            websocket.deleteTask(task);
            exit();
        }
    }

    /**
     * logic for adding a new subtask
     */
    public void addSubtask() {
        if (task == null) {
            alertUtils.alertError("No task to add subtask to!");
            return;
        }
        mainCtrl.showAddSubtask(task);
    }

    /**
     * getter for the Children manager of the subtask list
     * @return
     */
    public ChildrenManager<Subtask, SubtaskCtrl> getSubtaskChildrenManager() {
        return subtaskChildrenManager;
    }

    /**
     * getter for the current task
     * @return the current task
     */
    public Task getTask() {
        return task;
    }

    /**
     * setter for the ChildrenManager of the subtask
     * @param subtaskChildrenManager the ChildrenManager to be set
     */
    public void setSubtaskChildrenManager(ChildrenManager<Subtask,
            SubtaskCtrl> subtaskChildrenManager) {
        this.subtaskChildrenManager = subtaskChildrenManager;
    }

    /**
     * getter for the Parent Websocket
     * @return the ParentWebsocketManager used
     */
    public ParentWebsocketManager<Subtask, SubtaskCtrl> getParentWebsocket() {
        return parentWebsocket;
    }

    /**
     * getter for the websocket of the entity used
     * @return the EntityWebsocketManager
     */
    public EntityWebsocketManager<Task> getEntityWebsocket() {
        return entityWebsocket;
    }

    /**
     * sets the current ParentWebsocket
     * @param parentWebsocket the ParentWebsocket to be set
     */
    public void setParentWebsocket(ParentWebsocketManager
                                           <Subtask, SubtaskCtrl>
                                           parentWebsocket) {
        this.parentWebsocket = parentWebsocket;
    }

    /**
     * sets the current Entity Websocket
     * @param entityWebsocket the websocket to be set
     */
    public void setEntityWebsocket(EntityWebsocketManager<Task>
                                           entityWebsocket) {
        this.entityWebsocket = entityWebsocket;
    }

    /**
     * sets the current task
     * @param task the task to be set
     */
    public void setTask(Task task) {
        this.task = task;
    }
}
