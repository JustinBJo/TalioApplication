package client.scenes;


import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Task;
import commons.TaskList;
import commons.User;
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

    ObservableList<Board> boardData;

    @FXML
    Label sceneTitle;
    @FXML
    ListView<Board> boards;
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

        boardData = FXCollections.observableArrayList();
        boards.setFixedCellSize(0);
        boards.setItems(boardData);
        boards.setCellFactory(new BoardCtrl(mainCtrl, server, this));

        mainCtrl.setUser(server.checkUser());

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
        boardData =
                FXCollections.observableList(mainCtrl.getUser().getBoards());
        lists.setItems(listData);
        tasks.setItems(taskData);
        boards.setItems(boardData);
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

    /**
     * add a task to the list
     */
    public void addTask() {
        mainCtrl.showAddTask();
    }

    /**
     * displays the join board scene
     */
    public void joinBoard() {
        mainCtrl.showJoinBoard();
    }

    /**
     * checks whether this user has already been registered
     */
    public void validateUser() {
        User u = server.checkUser();
        mainCtrl.setUser(u);
    }

}

