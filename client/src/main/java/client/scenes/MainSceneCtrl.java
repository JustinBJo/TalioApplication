package client.scenes;

import client.utils.ChildrenManager;
import client.utils.ErrorUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.TaskList;
import commons.User;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.util.*;

public class MainSceneCtrl {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;
    private ChildrenManager<TaskList, TaskListCtrl> taskListChildrenManager;
    private ChildrenManager<Board, BoardCtrl> boardListChildrenManager;

    private final long defaultBoardID;

    private Board activeBoard;

    @FXML
    Label sceneTitle;
    @FXML
    VBox boardsContainer;
    @FXML
    HBox taskListsContainer;

    @FXML
    MenuItem createBoardMenu;
    @FXML
    MenuItem renameBoardMenu;
    @FXML
    MenuItem deleteBoardMenu;
    @FXML
    MenuItem joinBoardMenu;
    @FXML
    MenuItem joinServerMenu;

    @FXML
    ImageView menuIcon;
    @FXML
    ImageView adminIcon;
    @FXML
    ImageView copyIcon;

    @FXML
    Label boardCode;
    @FXML
    Label serverAddr;

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
     * This is called only once by the FXML builder,
     * after FXML components are initialized.
     */
    public void initialize() {
        // Create children manager (needs FXML container)
        validateUser();
        Board defaultBoard = server.getBoardById(defaultBoardID);
        if (!mainCtrl.getUser().getBoards().contains(defaultBoard)) {
            mainCtrl.getUser().getBoards().add(defaultBoard);
            server.saveUser(mainCtrl.getUser());
        }

        this.taskListChildrenManager = new ChildrenManager<>(
                taskListsContainer,
                TaskListCtrl.class,
                "TaskList.fxml"
        );

        this.boardListChildrenManager = new ChildrenManager<>(
                boardsContainer,
                BoardCtrl.class,
                "Board.fxml"
        );

        Image menu = new Image(getClass()
                .getResourceAsStream("/client/images/menuicon.png"));
        menuIcon.setImage(menu);

        Image admin = new Image(getClass()
                .getResourceAsStream("/client/images/adminicon.png"));
        adminIcon.setImage(admin);

        Image copy = new Image(getClass()
                .getResourceAsStream("/client/images/copyicon.png"));
        copyIcon.setImage(copy);



        // Set default board as current board (needs FXML title)
        setActiveBoard(server.getDefaultBoard());
    }

    /**
     * @return board currently shown in scene
     */
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
        boardCode.setText(activeBoard.getCode());

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
        for (TaskListCtrl taskListCtrl :
                taskListChildrenManager.getChildrenCtrls()) {
            taskListCtrl.refresh();
        }

        if (!mainCtrl.isAdmin()) {
        List<Board> joinedBoards = new ArrayList<>();
        boardListChildrenManager.updateChildren(joinedBoards);
        joinedBoards = mainCtrl.getUser().getBoards();
        boardListChildrenManager.updateChildren(joinedBoards); }
        else {
            List<Board> boards = new ArrayList<>();
            boardListChildrenManager.updateChildren(boards);
            boards = server.getAllBoards();
            boardListChildrenManager.updateChildren(boards);

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
            ErrorUtils.alertError("You cannot rename the default board!");
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
        if (!mainCtrl.isAdmin()) {
        if (activeBoard.getId() == defaultBoardID) {
            ErrorUtils.alertError("You cannot delete the default board!");
            return;
        }

        boolean confirmation = server.confirmDeletion("board");

        // Check the user's response and perform the desired action
        if (confirmation) {
            Board b = activeBoard;
            mainCtrl.getUser().getBoards().remove(b);
            server.saveUser(mainCtrl.getUser());
            setActiveBoard(server.getDefaultBoard());
            System.out.println(server.deleteBoard(b)); } }
        else {
            Board b = activeBoard;
            deleteBoard(b);
            refresh();
        }
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
     * set the server address to be displayed
     * @param address the address to be displayed
     */
    public void setServerAddr(String address) {
        this.serverAddr.setText(address);
    }

    /**
     * add a list to the list
     */
    public void addList() {
        mainCtrl.showAddList();
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

    /**
     * functionality for the "admin" icon button
     */
    public void adminPassword() {
        if (!mainCtrl.isAdmin()) {
        mainCtrl.showAdmin(); }
        else {
            boolean accept = server.confirmRevertAdmin();

            if (accept) {
                mainCtrl.setAdmin(false);
                mainCtrl.setUser(server.checkUser());
                mainCtrl.showMain();
            }
        }
    }

    /**
     * deletes a given board from the system and all the users' joined boards
     * @param b the board to be deleted
     */
    public void deleteBoard(Board b) {
        if (b.getId() == defaultBoardID) {
            ErrorUtils.alertError("You cannot delete the default board!");
            return;
        }
        boolean confirmation = server.confirmDeletion("Board");

        if (confirmation) {
            for (User u : server.getAllUsers()) {
                if (u.getBoards().contains(b)) {
                    u.getBoards().remove(b);
                    server.saveUser(u);
                }
            }
            setActiveBoard(server.getDefaultBoard());
            System.out.println(server.deleteBoard(b));
        }

    }
}

