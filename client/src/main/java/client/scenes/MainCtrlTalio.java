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

    public void initialize(Stage primaryStage, Pair<ConnectScreenCtrl, Parent> connect,
                           Pair<MainSceneCtrl, Parent> mainScene, Pair<AddListCtrl, Parent> addList, Pair<AddTaskCtrl, Parent> addTask) {
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

    public void showConnect(){
        primaryStage.setTitle("Connect to a server");
        primaryStage.setScene(connect);
    }

    public void showMain(){
        primaryStage.setTitle("Talio: Lists");
        primaryStage.setScene(mainScene);
    }

    public void showAddList(){
        primaryStage.setTitle("Add a new List");
        primaryStage.setScene(addListScene);
    }

    public void showAddTask(){
        primaryStage.setTitle("Add a new task");
        primaryStage.setScene(addTaskScene);
    }

}
