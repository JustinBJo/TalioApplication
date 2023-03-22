package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Task;
import commons.TaskList;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;



public class MainCtrlTalio {

    private Stage primaryStage;
    private ServerUtils server;

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

    CardCtrl cardCtrl;
    Scene cardScene;

    RenameCtrl renameCtrl;
    Scene renameScene;

    EditTaskCtrl editTaskCtrl;
    Scene editTaskScene;


    private TaskList currentTaskList;

    private Board activeBoard;

    private Task currentTask;


    /**
     * initialize the main controller
     * @param primaryStage    the primary stage
     * @param connect         the "connect" screen
     * @param mainScene       the main screen
     * @param addTitledEntity the "add titled entity" screen
     * @param renameTaskList  the "rename list" screen
     */
    public void initialize(Stage primaryStage,
                           ServerUtils server,
                           Pair<ConnectScreenCtrl, Parent> connect,
                           Pair<MainSceneCtrl, Parent> mainScene,
                           Pair<AddTitledEntityCtrl, Parent> addTitledEntity,
                           Pair<AddTaskCtrl, Parent> addTask,
                           Pair<TaskListCtrl, Parent> taskList,
                           Pair<CardCtrl, Parent> card,
                           Pair<RenameCtrl, Parent> renameTaskList,
                           Pair<EditTaskCtrl, Parent> editTask) {
        this.primaryStage = primaryStage;
        this.server = server;

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

        this.cardCtrl = card.getKey();
        this.cardScene = new Scene(card.getValue());

        this.renameCtrl = renameTaskList.getKey();
        this.renameScene = new Scene(renameTaskList.getValue());

        this.editTaskCtrl = editTask.getKey();
        this.editTaskScene = new Scene(editTask.getValue());

        setActiveBoard(server.getDefaultBoard());
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
     * Returns the current Task we want to edit
     * @return current task
     */
    public Task getCurrentTask() {
        return currentTask;
    }

    /**
     * Updates title and description of current task
     * @param task
     */
    public void setCurrentTask(Task task) {
        currentTask = task;
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
     * Switches scene to "Edit Task" scene,
     * that shows the current task's information.
     */
    public void showEditTask(Task task) throws IOException {
        final FXMLLoader fxmlLoader =
                new FXMLLoader(getClass().getResource("EditTask.fxml"));
        setCurrentTask(task);

        final Pane root = fxmlLoader.load();
        ObservableList<Node> children = root.getChildren();

        //Pane root1 = (Pane) editTaskScene.getWindow().getScene().getRoot();
        //ObservableList<Node> children= root1.getChildren();


        for (Node child : children) {
            if (child.getId() != null) {
                if (child.getId().equals("currentTitle")) {
                    Label currentTitle = (Label) child;
                    currentTitle.setText(task.getTitle());
                }
                if (child.getId().equals("currentDescription")) {
                    Label currentDescription = (Label) child;
                    currentDescription.setText(task.getDescription());
                }
            }
        }

        Scene editTaskScene = new Scene(root, 570, 310);

        primaryStage.setTitle("Edit Task");
        primaryStage.setScene(editTaskScene);
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
}