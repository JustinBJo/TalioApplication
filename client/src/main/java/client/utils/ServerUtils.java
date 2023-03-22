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

import commons.Board;
import commons.Task;
import commons.TaskList;

import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    /**
     * get task list
     *
     * @return the task list
     */
    public List<TaskList> getTaskList() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("tasklist") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<TaskList>>() {
                });
    }

    /**
     * add a task list to the server
     * @param taskList  the task list
     * @param board     the board that the task list belongs to
     * @return          the added task list, null if failed to add
     */
    public TaskList addTaskList(TaskList taskList, Board board) {

        // Add task list to repository
        Response addListResponse = ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("tasklist") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
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
        Response linkBoardResponse = linkTaskListToBoard(board, addedList.getId());

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
     * Link task list that is already in repository to a board that is also already in repository
     * @param board board that will be linked to the task list
     * @param taskListId if of the task list that wll be linked to the board
     * @return the endpoint's Response
     */
    private Response linkTaskListToBoard(Board board, long taskListId) {
            return ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("board/addTaskList/" + board.getId() + "/" + taskListId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
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
     * @return the added task, in order for future operations
     * with it to be possible
     */
    public Task addTask(Task task) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("tasks") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(task, APPLICATION_JSON), Task.class);
    }

    /**
     * Deletes a tasklist from the server
     *
     * @param taskList the tasklist to be deleted
     * @return the tasklist which was deleted
     */
    public String deleteTaskList(TaskList taskList) {

        // Unlink task list from board
        Response unlinkListResponse = ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("board/removeTaskList/" + taskList.getId()) //
                .request(APPLICATION_JSON).accept(APPLICATION_JSON) //
                .put(Entity.entity(taskList, APPLICATION_JSON));

        // If failed to unlink list, exit now
        if (unlinkListResponse.getStatus() != Response.Status.OK.getStatusCode()) {
            unlinkListResponse.close();
            return "Failed to unlink task list from board";
        }

        Board unlinkedBoard = unlinkListResponse.readEntity(Board.class);
        unlinkListResponse.close();

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
        String res = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("board/delete/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(String.class);

        System.out.println(res);
        return res;
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
}
