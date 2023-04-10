package client.scenes;

import client.utils.AlertUtils;
import client.utils.ServerUtils;
import client.utils.WebsocketUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Subtask;
import commons.Task;
import commons.TaskList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

public class AddTitledEntityCtrl {

    enum Type {
        TaskList,
        Board,
        RenameTaskList,
        RenameBoard,
        Subtask,
        RenameSubtask
    }

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;
    private final AlertUtils alertUtils;
    private final WebsocketUtils websocket;

    private Type type;
    private TaskList taskListToEdit;

    private Subtask subtaskToEdit;

    private Task currentTask;

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
    public AddTitledEntityCtrl(ServerUtils server,
                               MainCtrlTalio mainCtrl,
                               AlertUtils alertUtils,
                               WebsocketUtils websocket) {
        this.alertUtils = alertUtils;
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.websocket = websocket;
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
            case Subtask:
                setHeader("Add a new subtask");
                break;
            case RenameSubtask:
                setHeader("Edit subtask");
                break;

            // Error handling (very unlikely, as it is an enum)
            default:
                pressCancel();
                break;
        }
    }

    /**
     * @param taskListToEdit task list which will be edited
     *                       if type is RenameTaskList
     */
    public void setTaskListToEdit(TaskList taskListToEdit) {
        this.taskListToEdit = taskListToEdit;
    }

    /**
     * @param subtask subtask which will be edited
     *                if type is RenameSubtask
     */
    public void setSubtaskToEdit(Subtask subtask) {
        this.subtaskToEdit = subtask;
    }

    /**
     * Setter for the current task
     * @param currentTask - the task we are adding the subtask to
     */
    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
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
                        alertUtils.alertError("No task list to edit!");
                        break;
                    }
                    editTaskList(title);
                    break;
                case RenameBoard:
                    editBoard(title);

                    break;
                case Subtask:
                    addNewSubtask(title);
                    textField.clear();
                    mainCtrl.showTaskDetails(currentTask);
                    return;
                //break;
                case RenameSubtask:
                    editSubtask(title);
                    textField.clear();
                    mainCtrl.showTaskDetails(currentTask);
                    return;

                // Error handling (very unlikely, as it is an enum)
                default:
                    alertUtils.alertError(
                        "Something went wrong, please try again!"
                    );
                    pressCancel();
                    break;
            }

        } catch (WebApplicationException e) {
            alertUtils.alertError(e.getMessage());
            return;
        }

        textField.clear();
        mainCtrl.showMain();
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
            alertUtils.alertError("Lists must be created within boards!");
            pressCancel();
            return;
        }

        websocket.addTaskList(taskList, parentBoard);
    }

    /**
     * Add a new board
     * @param title board title
     */
    private void addNewBoard(String title) {
        Board board = new Board(title);

        board = server.addBoard(board);
        mainCtrl.setActiveBoard(board);
        if (!mainCtrl.isAdmin()) {
            mainCtrl.getUser().addBoard(board);
            websocket.saveUser(mainCtrl.getUser()); }
        else {
            mainCtrl.showAdminBoards();
        }
    }

    private void editTaskList(String title) {
       websocket.updateTaskList(taskListToEdit, title);
    }

    private void editBoard(String title) {
       websocket.updateBoard(
            mainCtrl.getActiveBoard(),
            title
        );

       if (!mainCtrl.isAdmin()) {
        int index = mainCtrl.getUser().getBoards()
                .indexOf(mainCtrl.getActiveBoard());
        mainCtrl.getUser().getBoards().get(index).setTitle(title);
        websocket.saveUser(mainCtrl.getUser()); }
       else {
           mainCtrl.showAdminBoards();
       }
    }

    /**
     * Add a new subtask
     * @param title subtask title
     */
    private void addNewSubtask(String title) {
        Subtask subtask = new Subtask(title, false);
        websocket.addSubtask(subtask, currentTask);
    }

    private void editSubtask(String title) {
        websocket.updateSubtask(subtaskToEdit, title);
    }
}
