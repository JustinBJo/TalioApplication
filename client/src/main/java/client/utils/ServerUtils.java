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
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import commons.*;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private WebsocketUtils websockets;
    private String server = "http://localhost:8080/";

    /**
     * @param websockets WebsocketUtils instance used in application
     */
    public void setWebsockets(WebsocketUtils websockets) {
        this.websockets = websockets;
    }

    /**
     * Stops the thread that's doing long polling from running
     */
    public void stopPollingThread() {
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel(true);
            EXEC.shutdownNow();
        }
    }

    /**
     * get the server url to empty string
     */
    public void resetServer() {
        stopPollingThread();
        server = "";
    }

    /**
     * set the server url by the client's input
     * @param url the input url
     */
    public void setServer(String url)
            throws IllegalArgumentException {
        try {
            ClientBuilder.newClient(new ClientConfig()) //
                    .target(url) //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .get();
        } catch (IllegalArgumentException e2) {
            throw new IllegalArgumentException("Invalid URL");
        } catch (ProcessingException e) {
            throw new ProcessingException("Server not found");
        }
        stopPollingThread();
        server = url.endsWith("/") ? url : url + "/";
        websockets.updateServer(server.substring(7));
        startPollingThread();
    }

    // methods for long polling ------------------------------------------------------

    private static final ExecutorService EXEC =
            Executors.newSingleThreadExecutor();

    private Future<?> runnable;
    private Consumer<TaskTransfer> pollingConsumer;

    /**
     * Start listening for an update in task parent
     * @param consumer action taken when task parent is updated
     */
    public void listenForUpdateTaskParent(Consumer<TaskTransfer> consumer) {
        pollingConsumer = consumer;
    }

    private void startPollingThread() {
        runnable = EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                var res = ClientBuilder.newClient(new ClientConfig())
                        .target(server).path("tasks/listen/updateParent/")
                        .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                        .get();
                if (res.getStatus() == 204) { // No content
                    continue;
                }

                var taskTransfer = res.readEntity(TaskTransfer.class);

                if (pollingConsumer != null) {
                    pollingConsumer.accept(taskTransfer);
                }
            }
        });
    }


    // methods for boards ------------------------------------------------------

    /**
     * gets the default board from the repository
     * @return default board
     */
    public Board getDefaultBoard() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("board/default")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Board>() {
                });
    }

    /**
     * gets the id of the default board in the system
     * @return the id of the default board
     */
    public long getDefaultId() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("board/defaultId")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Long>() {
                });
    }

    /**
     * get task list of the give board
     * @param boardId the board to fetch the tasklists
     * @return the tasklists of the board
     */
    public List<TaskList> getBoardData(long boardId) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("board/" + boardId + "/tasklist")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<TaskList>>(){
                });
    }

    /**
     * return board from database based on its code
     * @param code the code of the board
     * @return the board
     */
    public Board getBoardByCode(String code) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("board/code/" + code)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Board.class);
    }

    /**
     * Uses board endpoint to ask server to add a new board
     *
     * @param board board to be added
     * @return added board
     */
    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("board")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    /**
     * returns a board based on its ID
     * @param id the id of the board
     * @return the board
     */
    public Board getBoardById(long id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("board/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Board.class);
    }


    // methods for tasklists ------------------------------------------------------

    /**
     * Gets all tasks belonging to a certain task list
     * @param taskList parent of desired tasks
     * @return list of tasks belonging to task list
     */
    public List<Task> getTaskListData(TaskList taskList) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("tasklist/getTasks/" + taskList.getId())
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(new GenericType<List<Task>>() {});
    }


    // methods for tasks ------------------------------------------------------

    /**
     * Gets all subtasks belonging to a certain task
     * @param task parent of desired subtasks
     * @return list of subtasks belonging to task
     */
    public List<Subtask> getTaskData(Task task) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("tasks/getSubtasks/" + task.getId())
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(new GenericType<List<Subtask>>() {});
    }

    /**
     * Method used to fetch the tasks from the database
     *
     * @return a List of all the tasks in the database
     */
    public List<Task> getTasks() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("tasks") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Task>>() {
                });
    }

    /**
     * Sets a new task list to hold a given task
     *
     * @param taskId    id of the changed task
     * @param newParent list that now holds the task
     */
    public void updateTaskParent(long taskId, TaskList newParent) {
        ClientBuilder.newClient(new ClientConfig()).target(server)
                .path("tasks/updateParent/" + taskId + "/" + newParent.getId())
                .request(APPLICATION_JSON).accept(APPLICATION_JSON) //
                .put(Entity.entity(newParent, APPLICATION_JSON), Task.class);
    }

    // methods for users ------------------------------------------------------

    /**
     * checks whether the user has already been
     * registered into the system
     * @return the existent user or a new one if
     * no existent one is found
     */
    public User checkUser() {
        String ip = ClientBuilder.newClient(new ClientConfig())
                .target(server).path("user/ip")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(String.class);

        List<User> users = ClientBuilder.newClient(new ClientConfig())
                .target(server).path("user")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<User>>() { });

        boolean exists = false;
        User user = new User(ip);
        for (User k : users) {
            if (k.getIp().equals(ip)) {
                exists = true;
                user = k;
            }
        }
        if (!exists) {
            ClientBuilder.newClient(new ClientConfig())
                    .target(server).path("user/add")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.entity(user, APPLICATION_JSON));
        }
        return user;
    }

    /**
     * gets all users in the database
     * @return the list of users in the database
     */
    public List<User> getAllUsers() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("user")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<User>>() { } );
    }


}
