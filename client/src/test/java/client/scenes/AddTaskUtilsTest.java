package client.scenes;

import client.utils.AddTaskUtils;
import client.utils.ServerUtils;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class AddTaskUtilsTest {
    private AddTaskUtils service;

    @BeforeEach
    void setup() {
        service = new AddTaskUtils(null, null);
        ServerUtils server = Mockito.mock(ServerUtils.class);
        MainCtrlTalio mainCtrl = Mockito.mock(MainCtrlTalio.class);
        service.setServer(server);
        service.setMainCtrl(mainCtrl);
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

    @Test
    void setMainCtrl() {
        MainCtrlTalio mainCtrl1 = Mockito.mock(MainCtrlTalio.class);
        service.setMainCtrl(mainCtrl1);
        MainCtrlTalio mainCtrl2 = service.getMainCtrl();
        assertEquals(mainCtrl1, mainCtrl2);
    }

    @Test
    void getMainCtrl() {
        MainCtrlTalio mainCtrl1 = Mockito.mock(MainCtrlTalio.class);
        service.setMainCtrl(mainCtrl1);
        MainCtrlTalio mainCtrl2 = service.getMainCtrl();
        assertEquals(mainCtrl1, mainCtrl2);
    }

}