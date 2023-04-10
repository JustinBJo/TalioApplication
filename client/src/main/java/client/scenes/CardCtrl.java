package client.scenes;

import client.utils.*;
import commons.Task;
import javafx.application.Platform;
import javafx.event.Event;
import commons.Subtask;
import commons.TaskList;
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
    private final CardService service;

    @FXML
    AnchorPane root;
    @FXML
    Label title;
    @FXML
    Label progress;
    @FXML
    Button delete;
    @FXML
    Button edit;
    @FXML
    Button moveUp;
    @FXML
    Button moveDown;
    @FXML
    ImageView editIcon;
    @FXML
    ImageView upIcon;
    @FXML
    ImageView downIcon;
    @FXML
    ImageView descriptionIndicator;


    /**
     * Main constructor for CardCtrl
     * @param service the card service
     */
    @Inject
    public CardCtrl(CardService service) {
        this.service = service;
    }

    /**
     * This is called only once by the FXML builder,
     * after FXML components are initialized.
     */
    public void initialize() {
        Image editIcon = new Image(getClass()
                .getResourceAsStream("/client/images/editicon.png"));
        Image upIcon = new Image(getClass()
                .getResourceAsStream("/client/images/arrowUp.png"));
        Image downIcon = new Image(getClass()
                .getResourceAsStream("/client/images/arrowDown.png"));
        Image descInd = new Image(getClass()
                .getResourceAsStream("/client/images/menuicon.png"));

        this.editIcon.setImage(editIcon);
        this.upIcon.setImage(upIcon);
        this.downIcon.setImage(downIcon);
        this.descriptionIndicator.setImage(descInd);

        // Set up drag and drop
        setupDragSource();
    }

    private void setupDragSource() {
        // What happens when starting to drag
        root.setOnDragDetected((MouseEvent event) -> {

            // Set content transferred on drag n drop
            Dragboard dragboard = root.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(service.getTask().getId()));
            dragboard.setContent(content);
            // Wrap up event
            event.consume();
        });

        // What happens after this is dropped
        root.setOnDragDone(Event::consume);

        Image descInd = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/menuicon.png")));
        this.descriptionIndicator.setImage(descInd);
    }

    /**
     * Set the Task entity that this controller holds
     * @param task the task that is being saved to this controller
     */
    public void setEntity(Task task) {
        service.setEntity(task);
        service.getWebsocket().registerForMessages(
                "/topic/subtask/add/" + task.getId(),
                Subtask.class,
                ignored -> Platform.runLater(this::setProgress)
        );
        Platform.runLater(() -> {
            title.setText(service.getTask().getTitle());

            if (service.getTask().getDescription() == null
                    || service.getTask().getDescription().isEmpty()) {
                descriptionIndicator.setImage(null);
            }

            setProgress();
        });
    }

    private void setProgress() {
        List<Subtask> subtasks = service.getServer()
                .getTaskData(service.getTask());
        if (subtasks == null || subtasks.size() == 0) {
            progress.setText("");
            return;
        }
        int progressNb = 0;
        for (Subtask subtask : subtasks) {
            if (subtask.isCompleted()) progressNb++;
        }
        progress.setText(progressNb + "/" + subtasks.size());
    }

    /**
     * @param taskList list that holds this task
     */
    public void setParentList(TaskList taskList) {
        service.setParentList(taskList);
    }

    /**
     * Show edit task scene for this task
     */
    public void editTask() {
        service.editTask();
    }

    /**
     * Used to delete a task from a list
     */
    public void deleteTask() {
        service.deleteTask();
    }

    /**
     * View the details of a task after clicking twice on the card
     */
    public void viewTask() {
        Task currentTask = service.getTask();
        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        service.getMainCtrl().showTaskDetails(currentTask);
                    }
                }
            }
        });
    }

    /**
     * Used to move a task up in the parent list
     */
    public void moveUp() {
        service.moveUp();
    }

    /**
     * Used to move a task down in the parent list
     */
    public void moveDown() {
        service.moveDown();
    }

}
