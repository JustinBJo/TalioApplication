package client.scenes;

import client.utils.AlertUtils;
import client.utils.EntityWebsocketManager;
import client.utils.WebsocketUtils;
import commons.Subtask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import java.util.Objects;

public class SubtaskCtrl
        implements IEntityRepresentation<Subtask> {

    private final WebsocketUtils websocket;
    private final AlertUtils alertUtils;
    private final MainCtrlTalio mainCtrl;

    private Subtask subtask;

    private final EntityWebsocketManager<Subtask> entityWebsocket;

    @FXML
    AnchorPane root;
    @FXML
    Label title;
    @FXML
    Button delete;
    @FXML
    CheckBox completed;
    @FXML
    private ImageView editIcon;
    @FXML
    private ImageView deleteIcon;

    /**
     * Main constructor for SubtaskCtrl
     *
     * @param mainCtrlTalio main controller of the application
     */
    @Inject
    public SubtaskCtrl(WebsocketUtils websocket,
                       AlertUtils alertUtils,
                       MainCtrlTalio mainCtrlTalio) {
        this.websocket = websocket;
        this.alertUtils = alertUtils;
        this.mainCtrl = mainCtrlTalio;

        this.entityWebsocket = new EntityWebsocketManager<>(
                websocket,
                "subtask",
                Subtask.class,
                this::setEntity
        );
    }

    /**
     * Set the Subtask entity that this controller holds
     * @param subtask the subtask that is being saved to this controller
     */
    public void setEntity(Subtask subtask) {
        this.subtask = subtask;
        if (subtask.getTitle() == null) {
            subtask.setTitle("Untitled");
        }

        Platform.runLater(() -> {
            title.setText(subtask.getTitle());
            completed.setSelected(subtask.isCompleted());
        });

        entityWebsocket.register(subtask.getId(), "update");
        entityWebsocket.register(subtask.getId(), "updateCompleteness");
    }

    /**
     * This is called only once by the FXML builder,
     * after FXML components are initialized.
     */
    public void initialize() {
        Image editIcon = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/editicon.png")));
        this.editIcon.setImage(editIcon);

        Image deleteIcon = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/deleteicon.png")));
        this.deleteIcon.setImage(deleteIcon);
    }


    /**
     * Used to delete a task from a list
     */
    public void deleteSubtask() {
        boolean confirmation = alertUtils.confirmDeletion("subtask");

        if (confirmation) {
            websocket.deleteSubtask(subtask);
        }
    }

    /**
     * Switches to the subtask's rename scene
     */
    public void edit() {
        mainCtrl.showRenameSubtask(subtask);
    }

    /**
     * Updates the status of the current subtask
     */
    public void completeness() {
        boolean newValue = completed.isSelected();
        subtask.setCompleted(newValue);
        websocket.updateSubtaskCompleteness(subtask, newValue);
    }
}
