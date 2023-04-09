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

    private final TaskListUtils utils;


    /**
     * Constructor with dependency injection
     */
    @Inject
    public TaskListCtrl(TaskListUtils utils) {
        this.utils = utils;
    }


    /**
     * Create children manager after FXML components are initialized
     */
    public void initialize() {
        utils.initialize(taskContainer);

        // Set up button icon
        Image editIcon = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/editicon.png")));
        this.editIcon.setImage(editIcon);

        // Set up drag and drop
        setupDropTarget();
    }

    /**
     * @return id of tasklist represented by this scene
     */
    public Long getId() {
        return utils.getId();
    }

    /**
     * @return children manager that handles this list's tasks
     */
    public ChildrenManager<Task, CardCtrl> getTaskChildrenManager() {
        return utils.getTaskChildrenManager();
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
            utils.setupDropTarget(taskId);

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
        utils.setEntity(taskList);
        String titleText = utils.getTaskList().getTitle();
        Platform.runLater(() -> {
            title.setText(titleText);
        });
    }

    /**
     * Deletes this task list
     */
    public void delete() {
        utils.delete();
    }

    /**
     * Switches to the rename scene and refreshes main scene
     */
    public void rename() {
        utils.rename();
    }


    /**
     * add a task to the list
     */
    public void addTask() {
        utils.addTask();
    }
}
