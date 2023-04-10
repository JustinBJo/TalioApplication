package client.utils;

import client.scenes.MainCtrlTalio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ConnectScreenUtilsTest {

    @Mock
    private MainCtrlTalio mainCtrl;
    @Mock
    private ServerUtils server;
    private ConnectScreenUtils sut;

    @BeforeEach
    void setup() {
        mainCtrl = Mockito.mock(MainCtrlTalio.class);
        server = Mockito.mock(ServerUtils.class);

        sut = new ConnectScreenUtils(mainCtrl, server);
    }

    @Test
    public void testConstructor() {
        assertEquals(sut.getMainCtrl(), mainCtrl);
        assertEquals(sut.getServer(), server);
    }

    @Test
    public void testSetServer() {
        sut.setServer("1234");
        verify(server, times(1)).setServer("1234");
    }

    @Test
    public void testChangeServer() {
        sut.changeServer("1234");

        verify(mainCtrl, times(1)).changeServer("1234");
        verify(mainCtrl, times(1)).showMain();
    }

    @Test
    public void getMainCtrlTest() {
        assertEquals(mainCtrl, sut.getMainCtrl());
    }

    @Test
    public void getServerTest() {
        assertEquals(server, sut.getServer());
    }


}