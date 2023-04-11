package client.scenes;

import client.utils.*;
import com.google.inject.Inject;
import commons.Board;
import commons.TaskList;
import commons.User;

import javafx.application.Platform;
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

public class MainSceneCtrl implements IEntityRepresentation<Board>  {

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;
    private final AlertUtils alertUtils;
    private final WebsocketUtils websocket;

    private ChildrenManager<TaskList, TaskListCtrl> taskListChildrenManager;
    private ChildrenManager<Board, BoardCtrl> boardListChildrenManager;

    private EntityWebsocketManager<User> joinedBoardsWebsocket;
    private ParentWebsocketManager<TaskList, TaskListCtrl> taskListsWebsocket;
    private EntityWebsocketManager<Board> entityWebsocket;

    private long defaultBoardID;

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
    public MainSceneCtrl(ServerUtils server,
                         MainCtrlTalio mainCtrl,
                         AlertUtils alertUtils,
                         WebsocketUtils websocket) {
        this.alertUtils = alertUtils;
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.websocket = websocket;
    }


    /**
     * This is called only once by the FXML builder,
     * after FXML components are initialized.
     */
    public void initialize() {
        // Create children manager (needs FXML container)
        this.taskListChildrenManager = new ChildrenManager<>(
                taskListsContainer,
                () -> BuildUtils.loadFXML(
                    TaskListCtrl.class,
                    "TaskList.fxml"
                )
        );

        this.boardListChildrenManager = new ChildrenManager<>(
                boardsContainer,
                () -> BuildUtils.loadFXML(
                    BoardCtrl.class,
                    "Board.fxml"
                )
        );

        // Long polling for drag and drop
        startLongPolling();

        // Create websocket managers
        this.entityWebsocket = new EntityWebsocketManager<>(
                websocket,
                "board",
                Board.class,
                this::setEntity
        );
        this.taskListsWebsocket = new ParentWebsocketManager<>(
                websocket,
                "taskList",
                TaskList.class,
                taskListChildrenManager
        );
        this.joinedBoardsWebsocket = new EntityWebsocketManager<>(
                websocket,
                "user",
                User.class,
                this::updateJoinedBoards
        );

        // Set button icons
        Image menu = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/menuicon.png")));
        menuIcon.setImage(menu);

        Image admin = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/adminicon.png")));
        adminIcon.setImage(admin);

        Image copy = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/copyicon.png")));
        copyIcon.setImage(copy);
    }

    /**
     * Handles a server change
     */
    public void changeServer() {
        this.defaultBoardID = server.getDefaultId();

        validateUser();
        Board defaultBoard = server.getBoardById(defaultBoardID);
        if (!mainCtrl.getUser().getBoards().contains(defaultBoard)) {
            mainCtrl.getUser().getBoards().add(defaultBoard);
            websocket.saveUser(mainCtrl.getUser());
        }

        // Reset user and board
        setEntity(server.getDefaultBoard());
        updateJoinedBoards(mainCtrl.getUser());
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
    public void setEntity(Board activeBoard) {
        this.activeBoard = activeBoard;

        Platform.runLater(() -> {
            sceneTitle.setText(activeBoard.getTitle());
            boardCode.setText(activeBoard.getCode());
        });

        taskListChildrenManager.updateChildren(
                server.getBoardData(activeBoard.getId())
        );

        entityWebsocket.register(activeBoard.getId(), "update");
        taskListsWebsocket.register(activeBoard.getId());

        // Go to default board if this board is deleted somewhere else
        websocket.registerForMessages(
                "/topic/board/delete/" + activeBoard.getId(),
                Integer.class,
                (status) -> {
                    if (status == 200) { // Delete successful
                        setEntity(server.getDefaultBoard());
                    }
                }
        );
    }

    private void startLongPolling() {
        server.listenForUpdateTaskParent(t -> {
            taskListChildrenManager.getChildrenCtrls().forEach(taskListCtrl -> {
                if (taskListCtrl.getId().equals(t.oldParentId)) {
                    taskListCtrl.getTaskChildrenManager()
                            .removeChild(t.oldTask);
                }

                if (taskListCtrl.getId().equals(t.newParentId)) {
                    taskListCtrl.getTaskChildrenManager()
                            .addOrUpdateChild(t.newTask);
                }
            });
        });
    }

    /**
     * go back to the connect screen
     * TODO: delete all the potential local storage,
     * since the user want to connect to a different server
     */
    public void back() {
        server.resetServer();
        mainCtrl.showConnect();
    }

    /**
     * Refresh the view, showing all task lists
     */
    public void updateJoinedBoards(User user) {
        boardListChildrenManager.updateChildren(new ArrayList<>());
        List<Board> joinedBoards = user.getBoards();
        boardListChildrenManager.updateChildren(joinedBoards);
    }

    /**
     * fills the overview with all boards in the database
     */
    public void adminBoards() {
        boardListChildrenManager.updateChildren(new ArrayList<>());
        List<Board> joinedBoards = server.getBoards();
        boardListChildrenManager.updateChildren(joinedBoards);
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
            alertUtils.alertError("You cannot rename the default board!");
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
        mainCtrl.deleteBoard(activeBoard);
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
     * Handles setting the current user
     */
    public void validateUser() {
        User u = server.checkUser();
        mainCtrl.setUser(u);
        joinedBoardsWebsocket.register(u.getId(), "save");
    }

    /**
     * functionality for the "admin" icon button
     */
    public void adminPassword() {
        if (!mainCtrl.isAdmin()) {
            mainCtrl.showAdmin();
        }
        else {
            boolean accept = alertUtils.confirmRevertAdmin();

            if (accept) {
                mainCtrl.setAdmin(false);
                mainCtrl.setUser(server.checkUser());
                updateJoinedBoards(mainCtrl.getUser());
                mainCtrl.setActiveBoard(server.getDefaultBoard());
                mainCtrl.showMain();
            }
        }
    }


}

