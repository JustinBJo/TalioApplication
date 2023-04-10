package client.utils;

import client.scenes.MainCtrlTalio;
import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardUtilsTest {

    @Mock
    private MainCtrlTalio mainCtrl;
    @Mock
    private ServerUtils server;
    @Mock
    private WebsocketUtils websocket;
    @Mock
    private AlertUtils alertUtils;

    private User user;

    private BoardUtils sut;

    @BeforeEach
    void setup() {
        mainCtrl = Mockito.mock(MainCtrlTalio.class);
        server = Mockito.mock(ServerUtils.class);
        websocket = Mockito.mock(WebsocketUtils.class);
        alertUtils = Mockito.mock(AlertUtils.class);

        user = new User();

        sut = new BoardUtils(mainCtrl, server, websocket, alertUtils);
    }

    @Test
    public void testConstructor() {
        assertEquals(sut.getMainCtrl(), mainCtrl);
        assertEquals(sut.getServer(), server);
        assertEquals(sut.getAlertUtils(), alertUtils);
        assertEquals(sut.getWebsocket(), websocket);
    }

    @Test
    public void testSetEntity() {
        Board b = new Board("a");
        sut.setEntity(b);
        assertEquals(sut.getBoard(), b);
    }

    @Test
    public void testSetEntityUntitled() {
        Board b = new Board();
        sut.setEntity(b);
        assertEquals(sut.getBoard().getTitle(), "Untitled");
    }

    @Test
    public void testLeaveDefault() {
        Board b = new Board();
        b.setId(1);
        when(server.getDefaultId()).thenReturn(b.getId());
        sut.setBoard(b);
        sut.leave();
        verify(alertUtils, times(1)).
                alertError("You cannot leave the default board!");
    }

    @Test
    public void testLeaveNotAdmin() {
        Board b = new Board();
        b.setId(1);
        sut.setBoard(b);
        user.getBoards().add(b);

        when(server.getDefaultId()).thenReturn(100L);
        when(mainCtrl.isAdmin()).thenReturn(false);
        when(mainCtrl.getUser()).thenReturn(user);
        when(server.getDefaultBoard()).thenReturn(b);
        when(mainCtrl.getActiveBoard()).thenReturn(b);

        sut.leave();

        assertFalse(user.getBoards().contains(b));
        verify(websocket, times(1)).saveUser(user);
        verify(mainCtrl, times(1)).setActiveBoard(b);
        assertNull(sut.getBoard());

    }

    @Test
    public void testLeaveAdmin() {
        Board b = new Board();
        b.setId(1);
        sut.setBoard(b);
        user.getBoards().add(b);

        when(server.getDefaultId()).thenReturn(100L);
        when(mainCtrl.isAdmin()).thenReturn(true);

        sut.leave();

        verify(mainCtrl, times(1)).deleteBoard(b);
        assertNull(sut.getBoard());
    }

    @Test
    public void testAccess() {
        Board b = new Board();
        b.setId(1);
        sut.setBoard(b);

        sut.access();

        verify(mainCtrl, times(1)).setActiveBoard(b);
    }

    @Test
    public void testGetMainCtrl() {
        assertEquals(sut.getMainCtrl(), mainCtrl);
    }

    @Test
    public void testGetServer() {
        assertEquals(sut.getServer(), server);
    }

    @Test
    public void testGetWebsocket() {
        assertEquals(sut.getWebsocket(), websocket);
    }

    @Test
    public void testGetAlertUtils() {
        assertEquals(sut.getAlertUtils(), alertUtils);
    }

    @Test
    public void testGetSetBoard() {
        Board b = new Board("test");
        sut.setBoard(b);

        assertEquals(sut.getBoard(), b);
    }


}