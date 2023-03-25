package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.TaskList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class AddTitledEntityCtrl {

    enum Type {
        TaskList,
        Board,
        RenameTaskList,
        RenameBoard
    }

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;

    private Type type;
    private TaskList taskListToEdit;

    @FXML
    Button cancel;
    @FXML
    Button confirm;
    @FXML
    TextField textField;
    @FXML
    Label header;

    /**
     * constructor
     * @param server the server utils
     * @param mainCtrl the main controller
     */
    @Inject
    public AddTitledEntityCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the scene for a certain titled entity type
     * @param type which titled entity will this scene add
     */
    public void initialize(Type type) {
        this.type = type;

        switch (type) {
            case TaskList:
                setHeader("Add new list");
                break;
            case Board:
                setHeader("Add new board");
                break;
            case RenameTaskList:
                setHeader("Edit list");
                break;
            case RenameBoard:
                setHeader("Edit current board");
                break;

            // Error handling (very unlikely, as it is an enum)
            default:
                pressCancel();
                break;
        }
    }

    public void setTaskListToEdit(TaskList taskListToEdit) {
        this.taskListToEdit = taskListToEdit;
    }

    /**
     * Sets the text in the scene's header
     * @param text new header text
     */
    private void setHeader(String text) {
        header.setText(text);
    }

    /**
     * Defines the behaviour of pressing the "cancel" button
     */
    public void pressCancel() {
        textField.clear();
        mainCtrl.mainSceneCtrl.refresh();
        mainCtrl.showMain();
    }

    /**
     * Defines the behaviour of pressing the "confirm" button
     */
    public void pressConfirm() {
        String title = textField.getText();

        if (title.isEmpty()) {
            // Do nothing if there's no title
            return;
        }

        try {

            // Do proper add according to entity type
            switch (type) {
                case TaskList:
                    addNewTaskList(title);
                    break;
                case Board:
                    addNewBoard(title);
                    break;
                case RenameTaskList:
                    if (taskListToEdit == null) {
                        alertError("No task list to edit!");
                        break;
                    }
                    editTaskList(title);
                    break;
                case RenameBoard:
                    editBoard(title);
                    break;

                // Error handling (very unlikely, as it is an enum)
                default:
                    alertError("Something went wrong, please try again!");
                    pressCancel();
                    break;
            }

        } catch (WebApplicationException e) {
            alertError(e.getMessage());
            return;
        }

        textField.clear();
        mainCtrl.mainSceneCtrl.refresh();
        mainCtrl.showMain();
    }

    /**
     * Creates an error alert with given text
     * @param text text shown in error alert
     */
    private void alertError(String text) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * Add a new task list
     * @param title task list title
     */
    private void addNewTaskList(String title) {
        TaskList taskList = new TaskList(title);
        Board parentBoard = mainCtrl.getActiveBoard();

        if (parentBoard == null) {
            // Error handling
            alertError("Lists must be created within boards!");
            pressCancel();
            return;
        }

        server.addTaskList(taskList, parentBoard);
        mainCtrl.mainSceneCtrl.refresh();
    }

    /**
     * Add a new board
     * @param title board title
     */
    private void addNewBoard(String title) {
        Board board = new Board(title);
        mainCtrl.setActiveBoard(server.addBoard(board));
    }

    private void editTaskList(String title) {
       server.updateTaskList(taskListToEdit, title);
    }

    private void editBoard(String title) {
        Board updatedBoard =
           server.updateBoard(
                mainCtrl.getActiveBoard(),
                title
            );
        mainCtrl.setActiveBoard(updatedBoard);
    }
}
