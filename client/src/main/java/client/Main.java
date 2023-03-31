/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.*;
import client.utils.BuildUtils;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * main method
     * @param args the command line arguments
     * @throws URISyntaxException if the URI is invalid
     * @throws IOException if the file cannot be read
     */
    public static void main(String[] args)
            throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        var connect = BuildUtils.loadFXML(
                ConnectScreenCtrl.class,
                "ConnectScreen.fxml"
        );

        var mainScene = BuildUtils.loadFXML(
                MainSceneCtrl.class,
                "MainScene.fxml"
        );

        var addTitledEntity = BuildUtils.loadFXML(
                AddTitledEntityCtrl.class,
                "AddTitledEntity.fxml"
        );

        var addTask = BuildUtils.loadFXML(
                AddTaskCtrl.class,
                "AddTask.fxml"
        );

        var viewTask = BuildUtils.loadFXML(
                TaskDetailsCtrl.class,
                "TaskDetails.fxml"
        );


        var editTask = BuildUtils.loadFXML(
                EditTaskCtrl.class,
                "EditTask.fxml"
        );

        var joinBoard = BuildUtils.loadFXML(
                JoinBoardCtrl.class,
                "JoinBoard.fxml"
        );



        var mainCtrl = BuildUtils.getInstance(MainCtrlTalio.class);
        mainCtrl.initialize(
                primaryStage,
                connect,
                mainScene,
                addTitledEntity,
                addTask,
                editTask,
                viewTask,
                joinBoard);
    }
}
