package client.scenes;


import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Task;
import commons.TaskList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class MainSceneCtrl {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;
    private final RenameCtrl renameCtrl;


    ObservableList<TaskList> listData;

    ObservableList<Task> taskData;

    @FXML
    Label sceneTitle;
    @FXML
    ListView boards;
    @FXML
    ListView<TaskList> lists;
    @FXML
    ListView<Task> tasks;
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
    public MainSceneCtrl(ServerUtils server, MainCtrlTalio mainCtrl,
                         RenameCtrl renameCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.renameCtrl = renameCtrl;
    }

    /**
     * initialize the scene with the listview elements as the TaskList scene
     */
    public void initialize() {
        listData = FXCollections.observableArrayList();
        lists.setFixedCellSize(0);
        lists.setItems(listData);
        lists.setCellFactory(new TaskListCtrl(server, this, mainCtrl,
                renameCtrl));
        refresh();
    }

    /**
     * go back to the connect screen
     */
    public void back() {
        mainCtrl.showConnect();
    }

    /**
     * refresh the list
     */
    public void refresh() {
        listData = FXCollections.observableList(server.getTaskList());
        taskData = FXCollections.observableList(server.getTasks());
        lists.setItems(listData);
        tasks.setItems(taskData);
    }

    private int i = 0;

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
        mainCtrl.showConnect();
        mainCtrl.setActiveBoard(null);
        server.deleteBoard(board);
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

    /**
     * add a task to the list
     */
    public void addTask() {
        mainCtrl.showAddTask();
    }

}

