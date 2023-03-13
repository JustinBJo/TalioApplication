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

import commons.Task;
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
     * Method used to fetch the tasks from the database
     * @return a List of all the tasks in the database
     */
    public List<Task> getTasks() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("tasks") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Task>>() {});
    }

    /**
     * Method used to insert a task into the database
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
}