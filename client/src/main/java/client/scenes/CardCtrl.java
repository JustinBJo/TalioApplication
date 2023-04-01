package client.scenes;

import client.utils.AlertUtils;
import client.utils.EntityWebsocketManager;
import client.utils.ServerUtils;
import client.utils.WebsocketUtils;
import commons.Subtask;
import commons.Task;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

public class CardCtrl implements IEntityRepresentation<Task> {

    private final WebsocketUtils websocket;
    private final MainCtrlTalio mainCtrl;
    private final AlertUtils alert;

    private Task task;

    private final EntityWebsocketManager<Task> entityWebsocket;

    @FXML
    AnchorPane root;
    @FXML
    Label title;
    @FXML
    Button delete;
    @FXML
    Button edit;
    @FXML
    ImageView editIcon;
    @FXML
    ImageView descriptionIndicator;


    /**
     * Main constructor for CardCtrl
     * @param mainCtrlTalio main controller of the application
     */
    @Inject
    public CardCtrl(MainCtrlTalio mainCtrlTalio,
                    AlertUtils alert,
                    WebsocketUtils websocket) {
        this.mainCtrl = mainCtrlTalio;
        this.alert = alert;
        this.websocket = websocket;

        this.entityWebsocket = new EntityWebsocketManager<>(
                websocket,
                "task",
                Task.class,
                this::setEntity
        );
    }

    /**
     * This is called only once by the FXML builder,
     * after FXML components are initialized.
     */
    public void initialize() {
        // Set up button icon
        Image editIcon = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/editicon.png")));
        this.editIcon.setImage(editIcon);

        // Set up drag and drop
        setupDragSource();
    }

    private void setupDragSource() {
        // What happens when starting to drag
        root.setOnDragDetected((MouseEvent event) -> {

            // Set content transferred on drag n drop
            Dragboard dragboard = root.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(task.getId()));
            dragboard.setContent(content);
            // Wrap up event
            event.consume();
        });

        // What happens after this is dropped
        root.setOnDragDone(event -> {
            mainCtrl.refreshBoard();
            event.consume();
        });

        Image descInd = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/menuicon.png")));
        this.descriptionIndicator.setImage(descInd);
    }

    /**
     * Set the Task entity that this controller holds
     * @param task the task that is being saved to this controller
     */
    public void setEntity(Task task) {
        this.task = task;
        if (task.getTitle() == null) {
            task.setTitle("Untitled");
        }

        Platform.runLater(() -> {
            title.setText(task.getTitle());
            if (task.getDescription().isEmpty()) {
                descriptionIndicator.setImage(null);
            }
        });

        entityWebsocket.register(task.getId(), "updateTitle");
        entityWebsocket.register(task.getId(), "updateDescription");
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
            mainCtrl.refreshBoard();
        }
    }

    /**
     * View the details of a task after clicking twice on the card
     */
    public void viewTask() {
        Task currentTask = task;
        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        mainCtrl.showTaskDetails(currentTask);
                    }
                }
            }
        });
    }

}
