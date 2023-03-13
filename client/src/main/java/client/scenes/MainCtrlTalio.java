package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrlTalio {

    private Stage primaryStage;

    ConnectScreenCtrl connectCtrl;
    Scene connect;

    MainSceneCtrl mainSceneCtrl;
    Scene mainScene;

    AddListCtrl addListCtrl;

    Scene addListScene;

    AddTaskCtrl addTaskCtrl;

    Scene addTaskScene;

    /**
     * initialize the main controller
     * @param primaryStage the primary stage
     * @param connect the connect screen
     * @param mainScene the main screen
     * @param addList the add list screen
     * @param addTask the add task screen
     */
    public void initialize(Stage primaryStage,
                           Pair<ConnectScreenCtrl, Parent> connect,
                           Pair<MainSceneCtrl, Parent> mainScene,
                           Pair<AddListCtrl, Parent> addList,
                           Pair<AddTaskCtrl, Parent> addTask) {

        this.primaryStage = primaryStage;

        this.connectCtrl = connect.getKey();
        this.connect = new Scene(connect.getValue());

        this.mainSceneCtrl = mainScene.getKey();
        this.mainScene = new Scene(mainScene.getValue());

        this.addListCtrl = addList.getKey();
        this.addListScene = new Scene(addList.getValue());

        this.addTaskCtrl = addTask.getKey();
        this.addTaskScene = new Scene(addTask.getValue());

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
        primaryStage.setScene(addListScene);
    }

    /**
     * Switches to the addTask scene
     */
    public void showAddTask() {
        primaryStage.setTitle("Add a new task");
        primaryStage.setScene(addTaskScene);
    }

}
