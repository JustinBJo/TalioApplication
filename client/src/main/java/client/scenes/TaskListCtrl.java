package client.scenes;

import client.utils.*;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.ArrayList;
import java.util.Objects;

public class TaskListCtrl implements IEntityRepresentation<TaskList> {
    @FXML
    AnchorPane root;
    @FXML
    Label title;
    @FXML
    Button deleteList;
    @FXML
    Button addTask;
    @FXML
    Button rename;
    @FXML
    VBox taskContainer;
    @FXML
    ImageView editIcon;

    private final Border highlightBorder = new Border(
            new BorderStroke(
                    Color.BLACK,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderStroke.THICK
            )
    );

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
    public TaskListCtrl(ServerUtils server, MainCtrlTalio mainCtrl, AlertUtils alertUtils, WebsocketUtils websocket) {
        this.alertUtils = alertUtils;
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.websocket = websocket;
    }


    /**
     * Create children manager after FXML components are initialized
     */
    public void initialize() {
        // Set up tasks manager
        taskChildrenManager = new ChildrenManager<>(
                taskContainer,
                CardCtrl.class,
                "Card.fxml"
        );

        // Set up button icon
        Image editIcon = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/editicon.png")));
        this.editIcon.setImage(editIcon);

        // Set up drag and drop
        setupDropTarget();

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

    private void setupDropTarget() {

        // Define behaviour when holding a dragged object here
        root.setOnDragOver(event -> {
            // Sets this as a drop target accepting copying or moving strings
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);

                // Displays a border on the task list
                root.setBorder(highlightBorder);
            }
            event.consume();
        });

        // Remove highlight when mouse exists list
        root.setOnDragExited(event -> root.setBorder(Border.EMPTY));

        // Define behaviour when dropping here
        root.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            long taskId = -1;
            try {
                // Try to get task id from dropped object
                if (!db.hasString()) throw new Exception();
                taskId = Long.parseLong(db.getString());
                if (taskId < 0) throw new Exception();
            } catch (Exception ignored) {
                // Tell source drop failed in case something goes wrong
                event.setDropCompleted(false);
                event.consume();
                return;
            }

            // Send update to server
            server.updateTaskParent(taskId, taskList);

            // Tell source drop was successful
            event.setDropCompleted(true);
            event.consume();
        });
    }

    /**
     * Set the TaskList instance that this Scene holds
     * @param taskList the TaskList instance to be set
     */
    public void setEntity(TaskList taskList) {
        this.taskList = taskList;
        if (taskList.getTitle() == null) {
            taskList.setTitle("Untitled");
        }
        Platform.runLater(() -> {
            title.setText(taskList.getTitle());
        });

        taskChildrenManager.updateChildren(server.getTaskListData(taskList));

        entityWebsocket.register(taskList.getId(), "update");
        parentWebsocket.register();
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
        mainCtrl.refreshBoard();
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
