package client.scenes;

import client.utils.*;
import commons.Task;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

class CardServiceTest {
    private CardService service;

    @BeforeEach
    void setup() {
        service = new CardService(null, null, null, null);
        ServerUtils server = Mockito.mock(ServerUtils.class);
        MainCtrlTalio mainCtrl = Mockito.mock(MainCtrlTalio.class);
        WebsocketUtils websocket = Mockito.mock(WebsocketUtils.class);
        AlertUtils alert = Mockito.mock(AlertUtils.class);
        service.setServer(server);
        service.setMainCtrl(mainCtrl);
        service.setAlert(alert);
        service.setWebsocket(websocket);
    }

    @Test
    void setParentList() {
        TaskList taskList = new TaskList("List #1");
        service.setParentList(taskList);
        assertEquals("TaskList{List #1, []}",
                service.getParentList().toString());
    }

    @Test
    void getParentList() {
        TaskList taskList = new TaskList("List #1");
        service.setParentList(taskList);
        assertEquals("TaskList{List #1, []}",
                service.getParentList().toString());
    }

    @Test
    void getTask() {
        Task task = new Task("Task #1", "", null, null);
        service.setTask(task);
        assertEquals("Task #1", service.getTask().getTitle());
    }

    @Test
    void setTask() {
        Task task = new Task("Task #1", "", null, null);
        service.setTask(task);
        assertEquals("Task #1", service.getTask().getTitle());
    }

    @Test
    void editTask() {
        Task task = new Task("Task #1", "", null, null);
        service.setTask(task);
        Mockito.doNothing().when(service.getMainCtrl())
                .showEditTask(service.getTask());
        service.editTask();
        Mockito.verify(service.getMainCtrl(), times(1))
                .showEditTask(service.getTask());
    }

    @Test
    void deleteTask() {
        Mockito.doNothing().when(service.getWebsocket())
                .deleteTask(null);
        service.deleteTask();
        Mockito.verify(service.getWebsocket(), times(0)) //Requires
                // a confirmation in order to delete and since none is given
                // nothing will actually be deleted
                .deleteTask(null);
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

    @Test
    void setWebsocket() {
        WebsocketUtils websocket1 = Mockito.mock(WebsocketUtils.class);
        service.setWebsocket(websocket1);
        WebsocketUtils websocket2 = service.getWebsocket();
        assertEquals(websocket1, websocket2);
    }

    @Test
    void getWebsocket() {
        WebsocketUtils websocket1 = Mockito.mock(WebsocketUtils.class);
        service.setWebsocket(websocket1);
        WebsocketUtils websocket2 = service.getWebsocket();
        assertEquals(websocket1, websocket2);
    }

    @Test
    void getAlert() {
        AlertUtils alert1 = Mockito.mock(AlertUtils.class);
        service.setAlert(alert1);
        AlertUtils alert2 = service.getAlert();
        assertEquals(alert1, alert2);
    }

    @Test
    void setAlert() {
        AlertUtils alert1 = Mockito.mock(AlertUtils.class);
        service.setAlert(alert1);
        AlertUtils alert2 = service.getAlert();
        assertEquals(alert1, alert2);
    }

}