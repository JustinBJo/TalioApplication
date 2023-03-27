package client.scenes;

import commons.Board;
import commons.Task;
import commons.TaskList;
import commons.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;




public class MainCtrlTalio {

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

    private BoardCtrl boardCtrl;
    private Scene boardScene;

    private User user;
    private Board activeBoard;


    /**
     * initialize the main controller
     * @param primaryStage    the primary stage
     * @param connect         the "connect" screen
     * @param mainScene       the main screen
     * @param addTitledEntity the "add titled entity" screen
     */
    public void initialize(Stage primaryStage,
                           Pair<ConnectScreenCtrl, Parent> connect,
                           Pair<MainSceneCtrl, Parent> mainScene,
                           Pair<AddTitledEntityCtrl, Parent> addTitledEntity,
                           Pair<AddTaskCtrl, Parent> addTask,
                           Pair<EditTaskCtrl, Parent> editTask,
                           Pair<TaskDetailsCtrl, Parent> viewTask,
                           Pair<JoinBoardCtrl, Parent> joinBoard,
                           Pair<BoardCtrl, Parent> board) {
        this.primaryStage = primaryStage;

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
        primaryStage.setTitle("Talio");
        mainSceneCtrl.refresh();
        primaryStage.setScene(mainScene);
    }

    /**
     * Refreshes the active board
     */
    public void refreshBoard() {
        mainSceneCtrl.refresh();
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
        mainSceneCtrl.setActiveBoard(board);
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

//    /**
//     * Sets current active board and updates the main scene accordingly
//     * @param activeBoard new active board
//     */
//    public void setActiveBoard(Board activeBoard) {
//        this.activeBoard = activeBoard;
//
//        if (mainSceneCtrl == null) {
//            mainSceneCtrl = new MainSceneCtrl(server, this, renameCtrl);
//            mainSceneCtrl.initialize(this.server);
//        }
//
//        if (activeBoard == null) {
//            mainSceneCtrl.sceneTitle.setText("Board X");
//        }
//        else {
//            mainSceneCtrl.sceneTitle.setText(activeBoard.getTitle());
//        }
//
//        mainSceneCtrl.refresh();
//        // TODO
//    }

    /**
     * Shows the detailed view of a task
     * @param task current task
     */
    public void showTaskDetails(Task task) {
        primaryStage.setTitle("Task Details");
        taskDetailsCtrl.setTask(task);
        primaryStage.setScene(viewTaskScene);
        primaryStage.setScene(viewTaskScene); }


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
     * getter for the mainCtrl
     * @return the currently used mainCtrl
     */
    public MainSceneCtrl getMainSceneCtrl() {
        return mainSceneCtrl;
    }
}
