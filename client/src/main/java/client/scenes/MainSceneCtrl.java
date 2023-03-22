package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Task;
import commons.TaskList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainSceneCtrl {

    private  ServerUtils server;
    private final MainCtrlTalio mainCtrl;
    private final RenameCtrl renameCtrl;

    List<TaskListCtrl> taskListCtrls;

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
    public void initialize(ServerUtils server) {
        this.server = server;

        taskListCtrls = new ArrayList<>();

        if (mainCtrl.getActiveBoard() == null) {
            mainCtrl.setActiveBoard(server.getDefaultBoard());
        }

        listData = FXCollections.observableArrayList();

        if (lists == null) {
            lists = new ListView<>();
        }

        lists.setFixedCellSize(0);
        lists.setItems(listData);
        lists.setCellFactory(taskListView -> new TaskListCell(new TaskListCtrl(
                server, this, mainCtrl, renameCtrl), this));
        refresh();
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
     * refresh the list
     */
    public void refresh() {
//        listData = FXCollections.observableList(server.getTaskList());
        listData = FXCollections.observableList(
                server.getBoardData(mainCtrl.getActiveBoard().getId()));
        taskData = FXCollections.observableList(server.getTasks());
        lists.setItems(listData);
        tasks.setItems(taskData);

        for (TaskListCtrl taskListCtrl : taskListCtrls) {
            taskListCtrl.refresh();
        }
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
        if (board.getId() == 1030) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("You cannot delete the default board!");
            alert.showAndWait();
            return;
        }
        mainCtrl.setActiveBoard(server.getDefaultBoard());
        server.deleteBoard(board);
        refresh();
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
     * Edit a task
     * @throws IOException
     */
    public void editTask() throws IOException {
        Task currentTask = tasks.getSelectionModel().getSelectedItem();
        mainCtrl.showEditTask(currentTask);
    }

}

