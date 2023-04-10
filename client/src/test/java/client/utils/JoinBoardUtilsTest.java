package client.utils;

import client.scenes.MainCtrlTalio;
import commons.Board;
import commons.User;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JoinBoardUtilsTest {

    @Mock
    private MainCtrlTalio mainCtrl;
    @Mock
    private WebsocketUtils websocket;
    @Mock
    private ServerUtils server;
    @Mock
    private AlertUtils alertUtils;

    private User user;

    private JoinBoardUtils sut;
    private Board b;

    @BeforeEach
    void setup() {
        mainCtrl = Mockito.mock(MainCtrlTalio.class);
        websocket = Mockito.mock(WebsocketUtils.class);
        server = Mockito.mock(ServerUtils.class);
        alertUtils = Mockito.mock(AlertUtils.class);
        b = new Board("1234", "test");
        user = new User();


        mainCtrl.setUser(user);

        when(mainCtrl.getUser()).thenReturn(user);
        when(server.getBoardByCode("1234")).thenReturn(b);

        sut = new JoinBoardUtils(mainCtrl, websocket, server, alertUtils);
    }

    @Test
    void testConstructor() {
        assertEquals(sut.getMainCtrl(), mainCtrl);
        assertEquals(sut.getWebsocket(), websocket);
        assertEquals(sut.getServer(), server);
        assertEquals(sut.getAlertUtils(), alertUtils);
    }

    @Test
    void testCancel() {
        sut.cancel();
        verify(mainCtrl, times(1)).showMain();
    }

    @Test
    void testJoinException() {
        when(server.getBoardByCode("1234"))
                .thenThrow(new WebApplicationException());
        sut.join("1234");
        verify(alertUtils, times(1))
                .alertError("There is no board with this code!");

    }

    @Test
    void testJoinAlreadyJoined() {

        user.getBoards().add(b);
        sut.join("1234");

        verify(mainCtrl, times(1)).setActiveBoard(b);
        verify(mainCtrl, times(1)).showMain();
    }

    @Test
    void testJoinAdded() {
        when(server.getBoardById(b.getId())).thenReturn(b);

        sut.join("1234");

        verify(mainCtrl, times(1)).setActiveBoard(b);
        verify(websocket, times(1)).saveUser(user);
        verify(mainCtrl, times(1)).showMain();
    }

    @Test
    void testJoinNotAdded() {
        when(server.getBoardById(b.getId())).thenReturn(null);

        sut.join("1234");

        verify(alertUtils, times(1))
                .alertError("There is no board with this code!");
    }

    @Test
    void testAddBoardTrue() {
        when(server.getBoardById(b.getId())).thenReturn(b);
        assertTrue(sut.addBoard(b));
        verify(server, times(1)).addBoard(b);
        assertTrue(user.getBoards().contains(b));
    }

    @Test
    void testAddBoardFalse() {
        when(server.getBoardById(b.getId())).thenReturn(null);
        assertFalse(sut.addBoard(b));
    }

    @Test
    void testGetMainCtrl() {
        assertEquals(sut.getMainCtrl(), mainCtrl);
    }

    @Test
    void testGetWebsocket() {
        assertEquals(sut.getWebsocket(), websocket);
    }

    @Test
    void testServer() {
        assertEquals(sut.getServer(), server);
    }

    @Test
    void testGetAlertUtils() {
        assertEquals(sut.getAlertUtils(), alertUtils);
    }



}