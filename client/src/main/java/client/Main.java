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

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.*;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

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

        var connect = FXML.load(ConnectScreenCtrl.class,
                "client", "scenes", "ConnectScreen.fxml");

        var mainScene = FXML.load(MainSceneCtrl.class,
                "client", "scenes", "MainScene.fxml");

        var addTitledEntity = FXML.load(AddTitledEntityCtrl.class,
                "client", "scenes", "AddTitledEntity.fxml");

        var addTask = FXML.load(AddTaskCtrl.class,
                "client", "scenes", "AddTask.fxml");

        var taskList = FXML.load(TaskListCtrl.class,
                "client", "scenes", "TaskList.fxml");

        var task = FXML.load(CardCtrl.class,
                "client", "scenes", "Card.fxml");

        var renameTaskList = FXML.load(
                RenameCtrl.class, "client", "scenes",
                "RenameEntity.fxml");

        var editTask = FXML.load(EditTaskCtrl.class,
                "client", "scenes", "EditTask.fxml");

        var viewTask = FXML.load(TaskDetailsCtrl.class,
                "client", "scenes", "TaskDetails.fxml");


        var mainCtrl =
                INJECTOR.getInstance(MainCtrlTalio.class);
        mainCtrl.initialize(primaryStage, connect, mainScene,
                addTitledEntity, addTask, taskList, task,
                renameTaskList, editTask, viewTask);
    }
}