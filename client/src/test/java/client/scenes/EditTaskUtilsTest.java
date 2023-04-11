package client.scenes;

import client.utils.EditTaskUtils;
import client.utils.MainCtrlTalio;
import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

class EditTaskUtilsTest {
    private EditTaskUtils service;

    @BeforeEach
    void setup() {
        service = new EditTaskUtils(null, null);
        ServerUtils server = Mockito.mock(ServerUtils.class);
        MainCtrlTalio mainCtrl = Mockito.mock(MainCtrlTalio.class);
        service.setServer(server);
        service.setMainCtrl(mainCtrl);
    }

    @Test
    void cancel() {
        Mockito.doNothing().when(service.getMainCtrl()).showMain();
        service.cancel();
        Mockito.verify(service.getMainCtrl(), times(1)).showMain();
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