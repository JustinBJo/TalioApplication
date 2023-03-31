package client.scenes;

import client.utils.ErrorUtils;
import client.utils.ServerUtils;
import commons.Task;
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

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;

    private Task task;
    private TaskList parentList;

    @FXML
    AnchorPane root;
    @FXML
    Label title;
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
     * @param server the server of the application
     * @param mainCtrlTalio main controller of the application
     */
    @Inject
    public CardCtrl(ServerUtils server,
                    MainCtrlTalio mainCtrlTalio) {
        this.server = server;
        this.mainCtrl = mainCtrlTalio;
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
        Image copyIcon = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/copyicon.png")));

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
        title.setText(task.getTitle());
        if (task.getDescription().isEmpty()) {
            descriptionIndicator.setImage(null);
        }
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
        boolean confirmation = server.confirmDeletion("task");

        // Check the user's response and perform the desired action
        if (confirmation) {
            server.deleteTask(task);
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
                        System.out.println("Double clicked");
                        mainCtrl.showTaskDetails(currentTask);
                    }
                }
            }
        });
    }

    /**
     * Used to move a task up in the parent list
     */
    public void moveUp() {
        TaskList currentTaskList = parentList;

        List<Task> currentTasks = currentTaskList.getTasks();
        int taskIndex = currentTasks.indexOf(task);
        taskIndex--;
        if (taskIndex < 0 || taskIndex >= currentTasks.size()) {
            ErrorUtils.alertError("You cannot move the task higher.");
            return;
        }
        currentTasks.remove(task);
        currentTasks.add(taskIndex, task);
        currentTaskList.setTasks(currentTasks);

        server.updateTasksInTasklist(currentTaskList, currentTasks);

        mainCtrl.refreshBoard();
    }

    /**
     * Used to move a task down in the parent list
     */
    public void moveDown() {
        TaskList currentTaskList = parentList;

        if (currentTaskList == null) {
            System.out.println("The task is not part of a list");
            mainCtrl.refreshBoard();
            return;
        }
        List<Task> currentTasks = currentTaskList.getTasks();
        int taskIndex = currentTasks.indexOf(task);
        taskIndex++;
        if (taskIndex < 0 || taskIndex >= currentTasks.size()) {
            ErrorUtils.alertError("You cannot move the task lower.");
            return;
        }
        currentTasks.remove(task);
        currentTasks.add(taskIndex, task);
        currentTaskList.setTasks(currentTasks);

        server.updateTasksInTasklist(currentTaskList, currentTasks);

        mainCtrl.refreshBoard();
    }

}
