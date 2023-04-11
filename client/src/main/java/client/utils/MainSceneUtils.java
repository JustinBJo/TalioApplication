package client.utils;

import client.scenes.BoardCtrl;
import client.scenes.IEntityRepresentation;
import client.scenes.MainCtrlTalio;
import client.scenes.TaskListCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.TaskList;
import commons.User;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MainSceneUtils implements IEntityRepresentation<Board> {
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

    /**
     * constructor
     *
     * @param mainCtrl the main controller
     */
    @Inject
    public MainSceneUtils(ServerUtils server,
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
    public void initialize(VBox boardsContainer, HBox taskListsContainer) {
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
    }

    /**
     * @return children manager that handles current board's lists
     */
    public ChildrenManager<TaskList, TaskListCtrl> getListChildrenManager() {
        return this.taskListChildrenManager;
    }

    /**
     * gets the board list manager for this scene
     * @return the board list manager
     */
    public ChildrenManager<Board, BoardCtrl> getBoardListChildrenManager() {
        return this.boardListChildrenManager;
    }

    /**
     * getter for server
     * @return server
     */
    public ServerUtils getServer() {
        return server;
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
     * Sets the new active board of scene
     * @param activeBoard the new active board
     */
    public void setActiveBoard(Board activeBoard) {
        this.activeBoard = activeBoard;
    }

    /**
     * getter for joined Boards websocket
     * @return the current joined boards websocket
     */
    public EntityWebsocketManager<User> getJoinedBoardsWebsocket() {
        return joinedBoardsWebsocket;
    }

    /**
     * setter for joined boards websocket
     * @param joinedBoardsWebsocket new joined boards websocket
     */
    public void setJoinedBoardsWebsocket(
            EntityWebsocketManager<User> joinedBoardsWebsocket) {
        this.joinedBoardsWebsocket = joinedBoardsWebsocket;
    }

    /**
     * setter for the default board id
     * @param defaultBoardID new id for default board
     */
    public void setDefaultBoardID(long defaultBoardID) {
        this.defaultBoardID = defaultBoardID;
    }

    /**
     * setter for the tasklist children manager
     * @param taskListChildrenManager new children manager
     */
    public void setTaskListChildrenManager(
            ChildrenManager<TaskList, TaskListCtrl> taskListChildrenManager) {
        this.taskListChildrenManager = taskListChildrenManager;
    }

    /**
     * setter for the tasklist websocket
     * @param taskListsWebsocket new tasklist websocket
     */
    public void setTaskListsWebsocket(
            ParentWebsocketManager<TaskList, TaskListCtrl> taskListsWebsocket) {
        this.taskListsWebsocket = taskListsWebsocket;
    }

    /**
     * setter for the entity websocket
     * @param entityWebsocket new entity websocket
     */
    public void setEntityWebsocket(
            EntityWebsocketManager<Board> entityWebsocket) {
        this.entityWebsocket = entityWebsocket;
    }

    /**
     * getter for websocket
     * @return the websocket
     */
    public WebsocketUtils getWebsocket() {
        return websocket;
    }

    /**
     * setter for the board list children manager
     * @param boardListChildrenManager new children manager
     */
    public void setBoardListChildrenManager(
            ChildrenManager<Board, BoardCtrl> boardListChildrenManager) {
        this.boardListChildrenManager = boardListChildrenManager;
    }

    /**
     * Sets current active board and updates the main scene accordingly
     * @param activeBoard new active board
     */
    public void setEntity(Board activeBoard) {
        this.activeBoard = activeBoard;

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
     * returns the board code
     */
    public String copyBoardCode() {
        return activeBoard.getCode();
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
                mainCtrl.showMain();
            }
        }
    }


}
