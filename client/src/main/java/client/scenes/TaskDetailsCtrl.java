package client.scenes;

import client.utils.*;
import com.google.inject.Inject;
import commons.Subtask;
import commons.Task;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Objects;
import java.util.List;


public class TaskDetailsCtrl {
    private final MainCtrlTalio mainCtrl;
    private final WebsocketUtils websocket;
    private final AlertUtils alertUtils;
    private final ServerUtils server;

    private ChildrenManager<Subtask, SubtaskCtrl> subtaskChildrenManager;
    private ParentWebsocketManager<Subtask, SubtaskCtrl> parentWebsocket;
    private EntityWebsocketManager<Task> entityWebsocket;

    private Task task;

    @FXML
    private Label title;
    @FXML
    private Label description;
    @FXML
    private ImageView editIcon;
    @FXML
    private ImageView deleteIcon;
    @FXML
    VBox subtaskContainer;

    /**
     * Constructor for the task details
     *
     * @param mainCtrl injects a mainCtrl object
     */
    @Inject
    public TaskDetailsCtrl(WebsocketUtils websocket,
                           MainCtrlTalio mainCtrl,
                           AlertUtils alertUtils,
                           ServerUtils server) {
        this.alertUtils = alertUtils;
        this.websocket = websocket;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * This is called only once by the FXML builder,
     * after FXML components are initialized.
     */
    public void initialize() {
        // Set up button icons
        Image editIcon = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/editicon.png")));
        this.editIcon.setImage(editIcon);

        Image deleteIcon = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/deleteicon.png")));
        this.deleteIcon.setImage(deleteIcon);

        // Set up children manager
        this.subtaskChildrenManager =
                new ChildrenManager<>(
                        subtaskContainer,
                        SubtaskCtrl.class,
                        "Subtask.fxml"
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
     * @param task task whose details are shown in the scene
     */
    public void setEntity(Task task) {
        if (task == null) return;

        this.task = task;
        if (task.getTitle() == null) {
            task.setTitle("Untitled");
        }

        Platform.runLater(() -> {
            title.setText(task.getTitle());
            description.setText(task.getDescription());
        });

        var subtasks = server.getTaskData(task);
        subtaskChildrenManager.updateChildren(subtasks);

        parentWebsocket.register(task.getId());
        entityWebsocket.register(task.getId(), "updateTitle");
        entityWebsocket.register(task.getId(), "updateDescription");
        setupCloseOnDelete();
    }

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
     * Method exit for exiting the detailed view of a task
     * returns to main scene
     */
    public void exit() {
        mainCtrl.showMain();
    }

    /**
     * Edit a task
     */
    public void editTask() {
        if (task == null) {
            alertUtils.alertError("No task to edit!");
            exit();
        }
        mainCtrl.showEditTask(task);
    }

    /**
     * Deletes the task from the detailed view and returns to the main scene
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
     * Adds a subtask to the current task
     */
    public void addSubtask() {
        if (task == null) {
            alertUtils.alertError("No task to add subtask to!");
            return;
        }
        mainCtrl.showAddSubtask(task);
    }

}
