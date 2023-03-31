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
import java.util.Optional;

import commons.Board;
import commons.Task;
import commons.TaskList;

import commons.User;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private static String SERVER = "http://localhost:8080/";


    /**
     * gets the default board from the repository
     * @return default board
     */
    public Board getDefaultBoard() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("board/default")
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
                .target(SERVER).path("board/defaultId")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Long>() {
                });
    }

    /**
     * Gets all task lists in repository
     *
     * @return a list containing all task lists
     */
    public List<TaskList> getAllTaskLists() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("tasklist") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<TaskList>>() {
                });
    }

    /**
     * get task list of the give board
     * @param boardId the board to fetch the tasklists
     * @return the tasklists of the board
     */
    public List<TaskList> getBoardData(long boardId) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("board/" + boardId + "/tasklist")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<TaskList>>(){
                });
    }

    /**
     * get the tasklist of the default board using only the api
     * @return the task list of the default board
     */
    public List<TaskList> getDefaultBoardData() {
        return getBoardData(getDefaultId());
    }

    /**
     * get the server url to empty string
     */
    public static void resetServer() {
        SERVER = "";
    }

    /**
     * set the server url by the client's input
     * @param url the input url
     */
    public static void setServer(String url)
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
        SERVER = url;
    }


    /**
     * add a task list to the server
     * @param taskList  the task list
     * @param board     the board that the task list belongs to
     * @return          the added task list, null if failed to add
     */
    public TaskList addTaskList(TaskList taskList, Board board) {

        // Add task list to repository
        Response addListResponse =
                ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("tasklist") //
                .request(APPLICATION_JSON).accept(APPLICATION_JSON) //
                .post(Entity.entity(taskList, APPLICATION_JSON));

        // If failed to add list, exit now
        if (addListResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            addListResponse.close();
            return null;
        }

        // Get added list
        TaskList addedList = addListResponse.readEntity(TaskList.class);

        addListResponse.close();

        // Link task list to board
        Response linkBoardResponse =
                linkTaskListToBoard(board, addedList.getId());

        int linkStatus = linkBoardResponse.getStatus();
        linkBoardResponse.close();

        // If succeeded to link list, wrap up and exit
        if (linkStatus == Response.Status.OK.getStatusCode()) {
            return addedList;
        }

        // If failed to link list,
        // remove it from repository to avoid lists with no parents
        removeTaskList(addedList.getId()).close();
        return null;
    }

    /**
     * Gets all tasks belonging to a certain task list
     * @param taskList parent of desired tasks
     * @return list of tasks belonging to task list
     */
    public List<Task> getTaskListData(TaskList taskList) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("tasklist/getTasks/" + taskList.getId())
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(new GenericType<List<Task>>() {});
    }

    /**
     * Link task list that is already in repository to a board that is also
     * already in repository
     * @param board board that will be linked to the task list
     * @param taskListId if of the task list that wll be linked to the board
     * @return the endpoint's Response
     */
    private Response linkTaskListToBoard(Board board, long taskListId) {
            return ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("board/addTaskList/" + board.getId() + "/" + taskListId)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .put(Entity.json(board));
    }

    /**
     * Method used to fetch the tasks from the database
     *
     * @return a List of all the tasks in the database
     */
    public List<Task> getTasks() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("tasks") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Task>>() {
                });
    }

    /**
     * Method used to insert a task into the database
     *
     * @param task the task to be added to the database
     * @param parentTaskList task list that will hold this task
     * @return the added task, in order for future operations
     * with it to be possible
     */
    public Task addTask(Task task, TaskList parentTaskList) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("tasks/" + parentTaskList.getId()) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(task, APPLICATION_JSON), Task.class);
    }

    /**
     * Displays an alert which asks for the confirmation of deletion
     * @param type the string which contains
     *             the type of the entity that the user is trying to delete
     * @return true if confirm is clicked, false otherwise
     */
    public boolean confirmDeletion(String type) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete " + type + "?");
        alert.setHeaderText("Delete " + type);
        alert.setContentText("Are you sure you want to delete this "
                + type + "?");

        ButtonType confirmButton = new ButtonType("Confirm");
        ButtonType cancelButton = new ButtonType("Cancel");

        // Remove the default buttons and add Confirm and Cancel buttons
        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == confirmButton) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * confirmation message for leaving admin mode
     * @return whether it has been confirmed or not
     */
    public boolean confirmRevertAdmin() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Leave admin mode?");
        alert.setHeaderText("Leave admin mode");
        alert.setContentText("Are you sure you want to leave the admin " +
                "mode? \n You will need to input the password again " +
                "to re-join!");
        ButtonType confirmButton = new ButtonType("Confirm");
        ButtonType cancelButton = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(confirmButton, cancelButton);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == confirmButton) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Deletes a tasklist from the server
     *
     * @param taskList the tasklist to be deleted
     * @return the tasklist which was deleted
     */
    public String deleteTaskList(TaskList taskList) {

        // Unlink task list from board
        Response unlinkResponse =
                ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("board/removeTaskList/" + taskList.getId()) //
                .request(APPLICATION_JSON).accept(APPLICATION_JSON) //
                .put(Entity.entity(taskList, APPLICATION_JSON));

        // If failed to unlink list, exit now
        if (unlinkResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            unlinkResponse.close();
            return "Failed to unlink task list from board";
        }

        Board unlinkedBoard = unlinkResponse.readEntity(Board.class);
        unlinkResponse.close();

        // Remove task list from repository
        Response removeListResponse = removeTaskList(taskList.getId());

        int removeStatus = removeListResponse.getStatus();
        String removeValue = removeListResponse.readEntity(String.class);
        removeListResponse.close();

        // If succeeded to link list, wrap up and exit
        if (removeStatus == Response.Status.OK.getStatusCode()) {
            return removeValue;
        }

        // If failed to remove list, link it back to board
        // to avoid lists with no parents
        linkTaskListToBoard(unlinkedBoard, taskList.getId()).close();
        return null;

    }


    /**
     * Remove task list from repository without unlinking it from board
     * @param taskListId id of task list to be removed
     */
    private Response removeTaskList(long taskListId) {
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("tasklist/delete/" + taskListId) //
                .request(APPLICATION_JSON).accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * Updates the name of the selected TaskList in the database
     *
     * @param taskList the TaskList to be renamed
     * @param newName  the name it should be renamed to
     * @return a TaskList entity
     */
    public TaskList updateTaskList(TaskList taskList, String newName) {
        long id = taskList.getId();
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("tasklist/update/" + id + "/" + newName)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(taskList, APPLICATION_JSON), TaskList.class);
    }

    /**
     * return board from database based on its code
     * @param code the code of the board
     * @return the board
     */
    public Board getBoardByCode(String code) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("board/code/" + code)
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
                .target(SERVER).path("board")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    /**
     * Update the title of the given board using the board/update endpoint
     *
     * @param board   the board that is being updated
     * @param newName the new name of the board
     * @return the updated board
     */
    public Board updateBoard(Board board, String newName) {
        long id = board.getId();
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("board/update/" + id + "/" + newName)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    /**
     * Delete an existing board using the board/delete endpoint
     *
     * @param board the board that is being removed
     * @return the removed board
     */
    public String deleteBoard(Board board) {
        long id = board.getId();
        System.out.println(id);
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("board/delete/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(String.class);

    }


    /**
     * returns a board based on its ID
     * @param id the id of the board
     * @return the board
     */
    public Board getBoardById(long id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("board/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Board.class);
    }

    /**
     * checks whether the user has already been
     * registered into the system
     * @return the existent user or a new one if
     * no existent one is found
     */
    public User checkUser() {
        String ip = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("user/ip")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(String.class);

        List<User> users = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("user")
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
                    .target(SERVER).path("user/add")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.entity(user, APPLICATION_JSON));
        }
        return user;
    }

    /**
     * saves the current state of the user in the database
     * @param user the user to be saved
     */
    public void saveUser(User user) {
     //   System.out.println(user.getBoards());
        //System.out.println(user.getBoards());
        List<Board> boards =  ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("user/save")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(user, APPLICATION_JSON),
                        new GenericType<List<Board>>() {});
        System.out.println(boards);
    }

    /**
     * gets all users in the database
     * @return the list of users in the database
     */
    public List<User> getAllUsers() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("user")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<User>>() { } );
    }



    /**
     * Update the title of the given task using the tasks/updateTitle endpoint
     *
     * @param task   the task that is being updated
     * @param newTitle the new title of the task
     * @return the updated task
     */
    public Task updateTaskTitle(Task task, String newTitle) {
        long id = task.getId();
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("tasks/updateTitle/" + id + "/" + newTitle)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(task, APPLICATION_JSON), Task.class);
    }

    /**
     * Update the description of the given task,
     * using the tasks/updateDescription endpoint
     *
     * @param task   the task that is being updated
     * @param newDescription the new description of the task
     * @return the updated task
     */
    public Task updateTaskDescription(Task task, String newDescription) {
        long id = task.getId();
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).
                path("tasks/updateDescription/" + id + "/" + newDescription)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(task, APPLICATION_JSON), Task.class);
    }

    /**
     * Deletes a task from the server
     * @param task the task to be deleted
     * @return the removed task
     */
    public String deleteTask(Task task) {
        long id = task.getId();
        String result = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER)
                .path("tasks/delete/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(String.class);

        System.out.println(result);
        return result;
    }

    /**
     * gets all boards in the database
     * @return the list of boards in the database
     */
    public List<Board> getAllBoards() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER)
                .path("board")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Board>>() { } );
    }
}
