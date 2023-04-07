package client.utils;

import client.scenes.CardCtrl;
import client.scenes.MainCtrlTalio;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class TaskListUtils {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;
    private final AlertUtils alertUtils;
    private final WebsocketUtils websocket;

    private EntityWebsocketManager<TaskList> entityWebsocket;
    private ParentWebsocketManager<Task, CardCtrl> parentWebsocket;
    private ChildrenManager<Task, CardCtrl> taskChildrenManager;

    private TaskList taskList;

    /**
     * Constructor with dependency injection
     */
    @Inject
    public TaskListUtils(ServerUtils server,
                        MainCtrlTalio mainCtrl,
                        AlertUtils alertUtils,
                        WebsocketUtils websocket) {
        this.alertUtils = alertUtils;
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.websocket = websocket;
    }

    /**
     * Create children manager after FXML components are initialized
     */
    public void initialize(VBox taskContainer) {
        // Set up tasks manager
        taskChildrenManager = new ChildrenManager<>(
                taskContainer,
                CardCtrl.class,
                "Card.fxml"
        );
        taskChildrenManager.setUpdatedChildConsumer(
                cardCtrl -> cardCtrl.setParentList(taskList)
        );

        // Set up websockets
        this.entityWebsocket = new EntityWebsocketManager<>(
                websocket,
                "taskList",
                TaskList.class,
                this::setEntity
        );
        this.parentWebsocket = new ParentWebsocketManager<>(
                websocket,
                "task",
                Task.class,
                taskChildrenManager
        );

    }

    /**
     * @return id of tasklist represented by this scene
     */
    public Long getId() {
        if (taskList == null) return -1L;
        return taskList.getId();
    }

    /**
     * @return children manager that handles this list's tasks
     */
    public ChildrenManager<Task, CardCtrl> getTaskChildrenManager() {
        return taskChildrenManager;
    }

    /**
     * Sets up this list for the drag and drop
     * @param taskId - the id of the dropped task
     */
    public void setupDropTarget(Long taskId) {
        server.updateTaskParent(taskId, taskList);
    }

    /**
     * Set the TaskList instance that this Scene holds
     * @param taskList the TaskList instance to be set
     */
    public String setEntity(TaskList taskList) {
        this.taskList = taskList;
        if (taskList.getTitle() == null) {
            taskList.setTitle("Untitled");
        }
        taskChildrenManager.clear();
        taskChildrenManager.updateChildren(server.getTaskListData(taskList));

        for (CardCtrl cardCtrl : taskChildrenManager.getChildrenCtrls()) {
            cardCtrl.setParentList(taskList);
        }

        entityWebsocket.register(taskList.getId(), "update");
        parentWebsocket.register(taskList.getId());
        websocket.registerForMessages(
                "/topic/taskList/updateChildren/" + taskList.getId(),
                TaskList.class,
                (tl) -> {
                    taskChildrenManager.updateChildren(new ArrayList<>());
                    taskChildrenManager.updateChildren(tl.getTasks());
                }
        );

        return taskList.getTitle();
    }

    /**
     * Deletes this task list
     */
    public void delete() {
        boolean confirmation = alertUtils.confirmDeletion("list");

        if (confirmation) {
            websocket.deleteTaskList(taskList);
            taskList = null;
        }
    }

    /**
     * Switches to the rename scene and refreshes main scene
     */
    public void rename() {
        mainCtrl.showRenameList(taskList);
    }


    /**
     * add a task to the list
     */
    public void addTask() {
        if (taskList == null) {
            alertUtils.alertError("No list to add task to!");
            return;
        }
        mainCtrl.showAddTask(taskList);
    }
}
