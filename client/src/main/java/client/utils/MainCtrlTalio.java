package client.utils;

import client.scenes.*;
import client.utils.AlertUtils;
import client.utils.ServerUtils;
import client.utils.WebsocketUtils;
import commons.Board;
import commons.Subtask;
import commons.Task;
import commons.TaskList;
import commons.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;




public class MainCtrlTalio {

    private AlertUtils alertUtils;
    private ServerUtils server;
    private WebsocketUtils websocket;

    private Stage primaryStage;

    private Scene connect;

    private MainSceneCtrl mainSceneCtrl;
    private Scene mainScene;

    private AddTitledEntityCtrl addTitledEntityCtrl;
    private Scene addTitledEntityScene;

    private AddTaskCtrl addTaskCtrl;
    private Scene addTaskScene;

    private EditTaskCtrl editTaskCtrl;
    private Scene editTaskScene;

    private TaskDetailsCtrl taskDetailsCtrl;
    private Scene viewTaskScene;


    private JoinBoardCtrl joinBoardCtrl;
    private Scene joinBoardScene;

    private AdminCtrl adminCtrl;
    private Scene adminScene;


    private User user;
    private boolean admin;


    /**
     * initialize the main controller
     * @param primaryStage    the primary stage
     * @param connect         the "connect" screen
     * @param mainScene       the main screen
     * @param addTitledEntity the "add titled entity" screen
     */
    public void initialize(Stage primaryStage,
                           ServerUtils server,
                           AlertUtils alertUtils,
                           WebsocketUtils websocket,
                           Pair<ConnectScreenCtrl, Parent> connect,
                           Pair<MainSceneCtrl, Parent> mainScene,
                           Pair<AddTitledEntityCtrl, Parent> addTitledEntity,
                           Pair<AddTaskCtrl, Parent> addTask,
                           Pair<EditTaskCtrl, Parent> editTask,
                           Pair<TaskDetailsCtrl, Parent> viewTask,
                           Pair<JoinBoardCtrl, Parent> joinBoard,
                           Pair<AdminCtrl, Parent> admin) {
        this.primaryStage = primaryStage;

        this.alertUtils = alertUtils;
        this.server = server;
        this.websocket = websocket;

        this.connect = new Scene(connect.getValue());

        this.mainSceneCtrl = mainScene.getKey();
        this.mainScene = new Scene(mainScene.getValue());

        this.addTitledEntityCtrl = addTitledEntity.getKey();
        this.addTitledEntityScene = new Scene(addTitledEntity.getValue());

        this.addTaskCtrl = addTask.getKey();
        this.addTaskScene = new Scene(addTask.getValue());

        this.editTaskCtrl = editTask.getKey();
        this.editTaskScene = new Scene(editTask.getValue());

        this.taskDetailsCtrl = viewTask.getKey();
        this.viewTaskScene = new Scene(viewTask.getValue());

        this.joinBoardCtrl = joinBoard.getKey();
        this.joinBoardScene = new Scene(joinBoard.getValue());

        this.adminCtrl = admin.getKey();
        this.adminScene = new Scene(admin.getValue());

        this.admin = false;
        showConnect();
        primaryStage.show();
    }

    /**
     * Sets a new server address for the application
     * @param newAddress new server's address
     */
    public void changeServer(String newAddress) {
        mainSceneCtrl.setServerAddr(newAddress);
        mainSceneCtrl.changeServer();
    }

    /**
     * show the connect screen
     */
    public void showConnect() {
        primaryStage.setTitle("Connect to a server");
        primaryStage.setScene(connect);
    }

    /**
     * show the main screen
     */
    public void showMain() {
        primaryStage.setTitle("Talio");
        primaryStage.setScene(mainScene);
    }

    /**
     * show the add list screen
     */
    public void showAddList() {
        primaryStage.setTitle("Add a new List");
        primaryStage.setScene(addTitledEntityScene);
        addTitledEntityCtrl.initialize(AddTitledEntityCtrl.Type.TaskList);
    }

    /**
     * changes to rename list scene
     */
    public void showRenameList(TaskList taskList) {
        primaryStage.setTitle("Rename the List");
        primaryStage.setScene(addTitledEntityScene);
        addTitledEntityCtrl.setTaskListToEdit(taskList);
        addTitledEntityCtrl.initialize(AddTitledEntityCtrl.Type.RenameTaskList);
    }

    /**
     * Gets the currently active board
     * @return the active board
     */
    public Board getActiveBoard() {
        return mainSceneCtrl.getActiveBoard();
    }

    /**
     * Sets the currently active board.
     * Does nothing if the parameter is null.
     */
    public void setActiveBoard(Board board) {
        if (board == null) return;
        mainSceneCtrl.setEntity(board);
    }

    /**
     * switches to addTask scene
     */
    public void showAddTask(TaskList parentTaskList) {
        primaryStage.setTitle("Add a new task");
        addTaskCtrl.setParentTaskList(parentTaskList);
        primaryStage.setScene(addTaskScene);
    }

    /**
     * Switches scene to "Add Board" scene
     */
    public void showAddBoard() {
        primaryStage.setTitle("Add a new board");
        primaryStage.setScene(addTitledEntityScene);
        addTitledEntityCtrl.initialize(AddTitledEntityCtrl.Type.Board);
    }

    /**
     * Switches scene to "Edit Task" scene,
     * that shows the current task's information.
     */
    public void showEditTask(Task task) {
        primaryStage.setTitle("Edit Task");
        editTaskCtrl.setEditedTask(task);
        primaryStage.setScene(editTaskScene);
    }


    /**
     * Switches scene to rename board scene
     */
    public void showRenameBoard() {
        primaryStage.setTitle("Rename the Board");
        primaryStage.setScene(addTitledEntityScene);
        addTitledEntityCtrl.initialize(AddTitledEntityCtrl.Type.RenameBoard);
    }

    /**
     * displays the join board scene
     */
    public void showJoinBoard() {
        primaryStage.setTitle("Join new board");
        primaryStage.setScene(joinBoardScene);
    }


    /**
     * Shows the detailed view of a task
     * @param task current task
     */
    public void showTaskDetails(Task task) {
        primaryStage.setTitle("Task Details");
        taskDetailsCtrl.setEntity(task);
        primaryStage.setScene(viewTaskScene);
    }


    /**
     * returns current user
     * @return current user
     */
    public User getUser() {
        return user;
    }

    /**
     * sets current user
     * @param user the user to be set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * switches to admin login scene
     */
    public void showAdmin() {
        primaryStage.setTitle("Enter admin password");
        primaryStage.setScene(adminScene);
    }
    /**
     * Switches scene to "Add Board" scene
     * @param task the task that the new subtask is assigned to
     */
    public void showAddSubtask(Task task) {
        primaryStage.setTitle("Add a new subtask");
        primaryStage.setScene(addTitledEntityScene);
        addTitledEntityCtrl.setCurrentTask(task);
        addTitledEntityCtrl.initialize(AddTitledEntityCtrl.Type.Subtask);
    }

    /**
     * checks whether the current user is an admin
     * @return true if it is admin, false otherwise
     */
    public boolean isAdmin() {
        return admin;
    }
    /**
     * changes to rename subtask scene
     */
    public void showRenameSubtask(Subtask subtask) {
        primaryStage.setTitle("Rename the subtask");
        primaryStage.setScene(addTitledEntityScene);
        addTitledEntityCtrl.setSubtaskToEdit(subtask);
        addTitledEntityCtrl.initialize(AddTitledEntityCtrl.Type.RenameSubtask);
    }

    /**
     * sets the current admin status
     * @param admin true if user is admin, false otherwise
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * connects the 'delete' board functionality in main scene controller
     * @param b the board to be deleted
     */
    public void deleteBoard(Board b) {
        if (b.getId().equals(server.getDefaultId())) {
            alertUtils.alertError("You cannot delete the default board!");
            return;
        }
        boolean confirmation = alertUtils.confirmDeletion("Board");

        if (confirmation) {
            for (User u : server.getAllUsers()) {
                if (u.getBoards().contains(b)) {
                    u.getBoards().remove(b);
                    websocket.saveUser(u);
                }
            }
            websocket.deleteBoard(b);
        }
    }

    /**
     * loads all boards in the system in the overview
     */
    public void showAdminBoards() {
        mainSceneCtrl.adminBoards();
    }
}
