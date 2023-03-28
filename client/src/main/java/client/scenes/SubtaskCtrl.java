package client.scenes;

import client.utils.ServerUtils;
import commons.Subtask;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;

public class SubtaskCtrl
        implements IEntityRepresentation<Subtask> {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;

    private Subtask subtask;

    @FXML
    AnchorPane root;
    @FXML
    Label title;
    @FXML
    Button delete;
    @FXML
    private ImageView editIcon;
    @FXML
    private ImageView deleteIcon;

    /**
     * Main constructor for SubtaskCtrl
     * @param server the server of the application
     * @param mainCtrlTalio main controller of the application
     */
    @Inject
    public SubtaskCtrl(ServerUtils server,
                       MainCtrlTalio mainCtrlTalio) {
        this.server = server;
        this.mainCtrl = mainCtrlTalio;
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
        title.setText(subtask.getTitle());
    }

    /**
     * This is called only once by the FXML builder,
     * after FXML components are initialized.
     */
    public void initialize() {
        Image editIcon = new Image(getClass()
                .getResourceAsStream("/client/images/editicon.png"));
        this.editIcon.setImage(editIcon);

        Image deleteIcon = new Image(getClass()
                .getResourceAsStream("/client/images/deleteicon.png"));
        this.deleteIcon.setImage(deleteIcon);
    }


    /**
     * Used to delete a task from a list
     */
    public void deleteSubtask() {
        server.deleteSubtask(subtask);
        mainCtrl.showTaskDetails(mainCtrl.getCurrentTask());
    }

    /**
     * Switches to the subtask's rename scene
     */
    public void edit() {
        mainCtrl.showRenameSubtask(subtask);
        mainCtrl.refreshBoard();
    }



}
