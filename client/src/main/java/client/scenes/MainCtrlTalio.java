package client.scenes;

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

    ConnectScreenCtrl connectCtrl;
    Scene connect;

    MainSceneCtrl mainSceneCtrl;
    Scene mainScene;

    AddTitledEntityCtrl addTitledEntityCtrl;
    Scene addTitledEntityScene;

    AddTaskCtrl addTaskCtrl;
    Scene addTaskScene;

    CardCtrl cardCtrl;
    Scene cardScene;

    EditTaskCtrl editTaskCtrl;
    Scene editTaskScene;

    TaskDetailsCtrl taskDetailsCtrl;
    Scene viewTaskScene;


    // TODO remove this
    private TaskList currentTaskList;
    private Task currentTask;


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
                           Pair<CardCtrl, Parent> card,
                           Pair<EditTaskCtrl, Parent> editTask,
                           Pair<TaskDetailsCtrl, Parent> viewTask) {
        this.primaryStage = primaryStage;

        this.connectCtrl = connect.getKey();
        this.connect = new Scene(connect.getValue());

        this.mainSceneCtrl = mainScene.getKey();
        this.mainScene = new Scene(mainScene.getValue());

        this.addTitledEntityCtrl = addTitledEntity.getKey();
        this.addTitledEntityScene = new Scene(addTitledEntity.getValue());

        this.addTaskCtrl = addTask.getKey();
        this.addTaskScene = new Scene(addTask.getValue());

        this.cardCtrl = card.getKey();
        this.cardScene = new Scene(card.getValue());

        this.editTaskCtrl = editTask.getKey();
        this.editTaskScene = new Scene(editTask.getValue());

        this.taskDetailsCtrl = viewTask.getKey();
        this.viewTaskScene = new Scene(viewTask.getValue());

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
        primaryStage.setScene(addTitledEntityScene);
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
        primaryStage.setTitle("Rename the Board");
        primaryStage.setScene(addTitledEntityScene);
        addTitledEntityCtrl.initialize(AddTitledEntityCtrl.Type.RenameBoard);
    }

    /**
     * Shows the detailed view of a task
     * @param task current task
     * @throws IOException if the task is not found
     */
    public void showTaskDetails(Task task) throws IOException {
        final FXMLLoader fxmlLoader =
                new FXMLLoader(getClass().getResource("TaskDetails.fxml"));
        setCurrentTask(task);

        final Pane root = fxmlLoader.load();
        ObservableList<Node> children = root.getChildren();

        for (Node child : children) {
            if (child.getId() != null) {
                if (child.getId().equals("title")) {
                    Label title = (Label) child;
                    title.setText(task.getTitle());
                }
                if (child.getId().equals("description")) {
                    Label description = (Label) child;
                    description.setText(task.getDescription());
                }
            }
        }

        Scene viewTaskScene = new Scene(root, 600, 400);

        primaryStage.setTitle("Task Details");
        primaryStage.setScene(viewTaskScene);
    }

}