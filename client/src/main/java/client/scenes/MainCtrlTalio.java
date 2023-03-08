package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrlTalio {

    private Stage primaryStage;

    private ConnectScreenCtrl connectCtrl;
    private Scene connect;


    private MainSceneCtrl mainSceneCtrl;
    private Scene mainScene;

    public void initialize(Stage primaryStage, Pair<ConnectScreenCtrl, Parent> connect, Pair<MainSceneCtrl, Parent>mainScene) {
        this.primaryStage = primaryStage;

        this.connectCtrl = connect.getKey();
        this.connect = new Scene(connect.getValue());

        this.mainSceneCtrl = mainScene.getKey();
        this.mainScene = new Scene(mainScene.getValue());

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
}
