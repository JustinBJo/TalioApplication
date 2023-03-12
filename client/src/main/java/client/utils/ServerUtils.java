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


import commons.TaskList;

import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    /**
     * get task list
     * @return the task list
     */
    public List<TaskList> getTaskList() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("tasklist") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<TaskList>>() {});
    }

    /**
     * add a task list to the server
     * @param taskList the task list
     * @return the task list
     */
    public TaskList addTaskList(TaskList taskList) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("tasklist") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(taskList, APPLICATION_JSON),
                        TaskList.class);
    }

    /**
     * Deletes a tasklist from the server
     * @param taskList the tasklist to be deleted
     * @return the tasklist which was deleted
     */
    public TaskList deleteTaskList(TaskList taskList) {
        long id = taskList.getId();
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("tasklist/delete/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(TaskList.class);
    }

    /**
     * Updates the name of the selected TaskList in the database
     * @param taskList the TaskList to be renamed
     * @param newName the name it should be renamed to
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
}