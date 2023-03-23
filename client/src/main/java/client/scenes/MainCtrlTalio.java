package client.scenes;


import commons.Board;
import commons.TaskList;
import commons.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;



public class MainCtrlTalio {

    private Stage primaryStage;

    ConnectScreenCtrl connectCtrl;
    Scene connect;

    MainSceneCtrl mainSceneCtrl;
    Scene mainScene;

    AddTitledEntityCtrl addTitledEntityCtrl;
    Scene addTitledEntityScene;

    AddTaskCtrl addTaskCtrl;

    Scene addTaskScene;

    TaskListCtrl taskListCtrl;
    Scene taskListScene;

    RenameCtrl renameCtrl;
    Scene renameScene;

    JoinBoardCtrl joinBoardCtrl;
    Scene joinBoardScene;

    BoardCtrl boardCtrl;
    Scene boardScene;

    private TaskList currentTaskList;

    private Board activeBoard;

    private List<Board> joinedBoards = new ArrayList<>();

    private User user;



    /**
     * initialize the main controller
     * @param primaryStage    the primary stage
     * @param connect         the "connect" screen
     * @param mainScene       the main screen
     * @param addTitledEntity the "add titled entity" screen
     * @param renameTaskList  the "rename list" screen
     */
    public void initialize(Stage primaryStage,
                           Pair<ConnectScreenCtrl, Parent> connect,
                           Pair<MainSceneCtrl, Parent> mainScene,
                           Pair<AddTitledEntityCtrl, Parent> addTitledEntity,
                           Pair<AddTaskCtrl, Parent> addTask,
                           Pair<TaskListCtrl, Parent> taskList,
                           Pair<RenameCtrl, Parent> renameTaskList,
                           Pair<JoinBoardCtrl, Parent> joinBoard,
                           Pair<BoardCtrl, Parent> board) {
        this.primaryStage = primaryStage;

        this.connectCtrl = connect.getKey();
        this.connect = new Scene(connect.getValue());

        this.mainSceneCtrl = mainScene.getKey();
        this.mainScene = new Scene(mainScene.getValue());

        this.addTitledEntityCtrl = addTitledEntity.getKey();
        this.addTitledEntityScene = new Scene(addTitledEntity.getValue());

        this.addTaskCtrl = addTask.getKey();
        this.addTaskScene = new Scene(addTask.getValue());

        this.taskListCtrl = taskList.getKey();
        this.taskListScene = new Scene(taskList.getValue());

        this.renameCtrl = renameTaskList.getKey();
        this.renameScene = new Scene(renameTaskList.getValue());

        this.joinBoardCtrl = joinBoard.getKey();
        this.joinBoardScene = new Scene(joinBoard.getValue());

        this.boardCtrl = board.getKey();
        this.boardScene = new Scene(board.getValue());


        showConnect();
        primaryStage.show();
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
        primaryStage.setTitle("Talio: Lists");
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
    public void showRenameList() {
        primaryStage.setTitle("Rename the List");
        primaryStage.setScene(renameScene);
        renameCtrl.initialize(RenameCtrl.Type.TaskList);
    }

    /**
     * Gets the currently active board
     * @return the active board
     */
    public Board getActiveBoard() {
        return this.activeBoard;
    }

    /**
     * Gets the current TaskList stored in the mainCtrl
     * @return the current task list
     */
    public TaskList getCurrentTaskList() {
        return currentTaskList;
    }

    /**
     * sets the current task list to the value given
     * @param currentTaskList the task list to be set
     */
    public void setCurrentTaskList(TaskList currentTaskList) {
        this.currentTaskList = currentTaskList;
    }

    /**
     * switches to addTask scene
     */
    public void showAddTask() {
        primaryStage.setTitle("Add a new task");
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
     * Switches scene to rename board scene
     */
    public void showRenameBoard() {
        if (this.activeBoard == null) {
            System.out.println("Cannot rename board: this is a dummy board!");
            return;
        }
        primaryStage.setTitle("Rename the Board");
        primaryStage.setScene(renameScene);
        renameCtrl.initialize(RenameCtrl.Type.Board);
    }

    /**
     * displays the join board scene
     */
    public void showJoinBoard() {
        primaryStage.setTitle("Join new board");
        primaryStage.setScene(joinBoardScene);
    }

    /**
     * Sets current active board and updates the main scene accordingly
     * @param activeBoard new active board
     */
    public void setActiveBoard(Board activeBoard) {
        this.activeBoard = activeBoard;

        if (activeBoard == null) {
            mainSceneCtrl.sceneTitle.setText("Board X");
        }
        else {
            mainSceneCtrl.sceneTitle.setText(activeBoard.getTitle());
        }

        mainSceneCtrl.refresh();
        // TODO
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
}
