package client.scenes;

import client.utils.AddTaskService;
import client.utils.ServerUtils;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class AddTaskServiceTest {
    private AddTaskService service;

    @BeforeEach
    void setup() {
        service = new AddTaskService(null, null);
        ServerUtils server = Mockito.mock(ServerUtils.class);
        service.setServer(server);
    }

    @Test
    void setParentTaskList() {
        TaskList taskList = new TaskList("List #1");
        service.setParentTaskList(taskList);
        assertEquals("TaskList{List #1, []}",
                service.getParentTaskList().toString());
    }

    @Test
    void getParentTaskList() {
        TaskList taskList = new TaskList("List #1");
        service.setParentTaskList(taskList);
        assertEquals("TaskList{List #1, []}",
                service.getParentTaskList().toString());
    }

    @Test
    void setServer() {
        ServerUtils server1 = Mockito.mock(ServerUtils.class);
        service.setServer(server1);
        ServerUtils server2 = service.getServer();
        assertEquals(server1, server2);
    }

    @Test
    void getServer() {
        ServerUtils server1 = Mockito.mock(ServerUtils.class);
        service.setServer(server1);
        ServerUtils server2 = service.getServer();
        assertEquals(server1, server2);
    }

}