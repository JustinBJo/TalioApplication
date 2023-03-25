package client.scenes;

import client.utils.ChildrenManager;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;


import java.util.*;

public class MainSceneCtrl {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;
    private ChildrenManager<TaskList, TaskListCtrl> taskListChildrenManager;

    private final long defaultBoardID;

    private Board activeBoard;

    @FXML
    Label sceneTitle;
    @FXML
    VBox boardsContainer;
    @FXML
    HBox taskListsContainer;
    @FXML
    Button renameBoard;
    @FXML
    Button removeBoard;
    @FXML
    Button copyCode;

    /**
     * constructor
     *
     * @param mainCtrl the main controller
     */
    @Inject
    public MainSceneCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;

        this.defaultBoardID = server.getDefaultId();
    }


    /**
     * This is called only once by the FXML builder, after FXML components are initialized.
     */
    public void initialize() {
        // Create children manager (needs FXML container)
        this.taskListChildrenManager = new ChildrenManager<>(
                taskListsContainer,
                TaskListCtrl.class,
                "TaskList.fxml"
        );

        // Set default board as current board (needs FXML title)
        setActiveBoard(server.getDefaultBoard());
    }

    public Board getActiveBoard() {
        return activeBoard;
    }

    /**
     * Sets current active board and updates the main scene accordingly
     * @param activeBoard new active board
     */
    public void setActiveBoard(Board activeBoard) {
        this.activeBoard = activeBoard;
        sceneTitle.setText(activeBoard.getTitle());

        refresh();
    }

    /**
     * go back to the connect screen
     * TODO: delete all the potential local storage,
     * since the user want to connect to a different server
     */
    public void back() {
        ServerUtils.resetServer();
        mainCtrl.showConnect();
    }

    /**
     * Refresh the view, showing all task lists
     */
    public void refresh() {
        List<TaskList> taskLists = server.getBoardData(activeBoard.getId());
        taskListChildrenManager.updateChildren(taskLists);
        for (TaskListCtrl taskListCtrl: taskListChildrenManager.getChildrenCtrls()) {
            taskListCtrl.refresh();
        }
    }

    /**
     * add a board to the list
     */
    public void addBoard() {
        mainCtrl.showAddBoard();
    }

    /**
     * Rename the current board
     */
    public void renameBoard() {
        if (activeBoard.getId() == defaultBoardID) {
            // TODO make error alerts a mainctrl method
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("You cannot rename the default board!");
            alert.showAndWait();
            return;
        }
        mainCtrl.showRenameBoard();
    }

    /**
     * Delete the active board
     * After deleting, go back to the connect screen
     * Behaviour after deletion can be changed in future implementations
     */
    public void removeBoard() {
        if (activeBoard.getId() == defaultBoardID) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("You cannot delete the default board!");
            alert.showAndWait();
            return;
        }
        server.deleteBoard(activeBoard);
        setActiveBoard(server.getDefaultBoard());
    }

    /**
     * Copies the code of current board
     * If the active board is null i.e. this is the default board,
     * then it copies an empty string
     */
    public void copyBoardCode() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        String code = activeBoard.getCode();
        content.putString(code);
        clipboard.setContent(content);

        System.out.println("The code for this board is copied!");
        System.out.println("Code: " + code);
    }

    /**
     * add a list to the list
     */
    public void addList() {
        mainCtrl.showAddList();
    }
}

