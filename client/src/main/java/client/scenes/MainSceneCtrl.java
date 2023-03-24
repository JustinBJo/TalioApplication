package client.scenes;

import client.utils.ChildrenManager;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;

public class MainSceneCtrl {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;
    private final ChildrenManager<TaskList> taskListChildrenManager;

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
     * @param mainCtrl the main controller
     */
    @Inject
    public MainSceneCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.taskListChildrenManager = new ChildrenManager<>(
                taskListsContainer,
                TaskListCtrl.class,
                "TaskList.fxml"
        );
    }

    /**
     * go back to the connect screen
     * TODO: delete all the protiential local storage,
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
        List<TaskList> taskLists = server.getAllTaskLists();
        taskListChildrenManager.updateChildren(taskLists);
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
        mainCtrl.showRenameBoard();
    }

    /**
     * Delete the active board
     * After deleting, go back to the connect screen
     * Behaviour after deletion can be changed in future implementations
     */
    public void removeBoard() {
        Board board = mainCtrl.getActiveBoard();
        if (board == null) {
            System.out.println("Cannot delete board: this is a dummy board!");
            return;
        }
        mainCtrl.setActiveBoard(null);
        server.deleteBoard(board);
        mainCtrl.showConnect();
    }

    /**
     * Copies the code of current board
     * If the active board is null i.e. this is the default board,
     * then it copies an empty string
     */
    public void copyBoardCode() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        Board board = mainCtrl.getActiveBoard();
        if (board == null) {
            System.out.println("This is the default board!");
        }
        else {
            String code = board.getCode();
            content.putString(code);
            clipboard.setContent(content);

            System.out.println("The code for this board is copied!");
            System.out.println("Code: " + code);
        }
    }

    /**
     * add a list to the list
     */
    public void addList() {
        mainCtrl.showAddList();
    }

}

