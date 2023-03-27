package client.scenes;

import client.utils.ServerUtils;
import commons.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import java.util.Optional;

public class CardCtrl implements IEntityRepresentation<Task> {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;

    private Task task;

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
        this.editIcon.setImage(editIcon);
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete task?");
        alert.setHeaderText("Delete Task");
        alert.setContentText("Are you sure you want to delete this task?");

        ButtonType confirmButton = new ButtonType("Confirm");
        ButtonType cancelButton = new ButtonType("Cancel");

        // Remove the default buttons and add Confirm and Cancel buttons
        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        // Check the user's response and perform the desired action
        if (result.isPresent() && result.get() == confirmButton) {
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

}
