package client.scenes;

import client.utils.AlertUtils;
import client.utils.MainCtrlTalio;
import client.utils.ServerUtils;
import client.utils.WebsocketUtils;
import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MainCtrlTalioTest {

    MainCtrlTalio sut;

    @BeforeEach
    void setUp() {
        sut = new MainCtrlTalio();
    }

    private Object getPrivateField(String fieldName) {
        try {
            Field field = MainCtrlTalio.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(sut);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPrivateField(String fieldName, Object value) {
        try {
            Field field = MainCtrlTalio.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(sut, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void changeServer() {
        var ctrl = Mockito.mock(MainSceneCtrl.class);
        try {
            setPrivateField("mainSceneCtrl", ctrl);
        } catch (RuntimeException e) {
            fail();
        }

        String newAddress = "13454326";
        sut.changeServer(newAddress);
        verify(ctrl).setServerAddr(newAddress);
        verify(ctrl).changeServer();
    }

    @Test
    void getActiveBoard() {
        var ctrl = Mockito.mock(MainSceneCtrl.class);
        Board b = Mockito.mock(Board.class);
        when(ctrl.getActiveBoard()).thenReturn(b);
        try {
            setPrivateField("mainSceneCtrl", ctrl);
        } catch (RuntimeException e) {
            fail();
        }

        assertEquals(b, sut.getActiveBoard());
    }

    @Test
    void setActiveBoard() {
        var ctrl = Mockito.mock(MainSceneCtrl.class);
        try {
            setPrivateField("mainSceneCtrl", ctrl);
        } catch (RuntimeException e) {
            fail();
        }

        Board b = Mockito.mock(Board.class);
        sut.setActiveBoard(b);
        verify(ctrl).setEntity(b);
    }

    @Test
    void getUser() {
        var user = Mockito.mock(User.class);
        try {
            setPrivateField("user", user);
        } catch (RuntimeException e) {
            fail();
        }

        assertEquals(user, sut.getUser());
    }

    @Test
    void setUser() {
        var user = Mockito.mock(User.class);
        sut.setUser(user);
        try {
            User set = (User) getPrivateField("user");
            assertEquals(user, set);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    void isAdmin() {
        try {
            setPrivateField("admin", true);
            assertTrue(sut.isAdmin());

            setPrivateField("admin", false);
            assertFalse(sut.isAdmin());
        } catch (RuntimeException e) {
            fail();
        }

    }

    @Test
    void setAdmin() {
        try {
            sut.setAdmin(true);
            assertTrue((Boolean) getPrivateField("admin"));

            sut.setAdmin(false);
            assertFalse((Boolean) getPrivateField("admin"));
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    void deleteDefaultBoard() {
        var board = Mockito.mock(Board.class);
        var server = Mockito.mock(ServerUtils.class);
        var alert = Mockito.mock(AlertUtils.class);
        var websocket = Mockito.mock(WebsocketUtils.class);

        Long defaultID = 512452151L;

        when(board.getId()).thenReturn(defaultID);
        when(server.getDefaultId()).thenReturn(defaultID);

        try {
            setPrivateField("server", server);
            setPrivateField("alertUtils", alert);
            setPrivateField("websocket", websocket);
        } catch (RuntimeException e) {
            fail();
        }

        sut.deleteBoard(board);
        verify(alert).alertError("You cannot delete the default board!");
        verify(alert, never()).confirmDeletion(anyString());
        verify(websocket, never()).deleteBoard(board);
    }

    @Test
    void deleteBoardCancel() {
        var board = Mockito.mock(Board.class);
        var server = Mockito.mock(ServerUtils.class);
        var alert = Mockito.mock(AlertUtils.class);
        var websocket = Mockito.mock(WebsocketUtils.class);

        Long defaultID = 512452151L;

        when(board.getId()).thenReturn(0L);
        when(server.getDefaultId()).thenReturn(defaultID);
        when(alert.confirmDeletion(anyString())).thenReturn(false);

        try {
            setPrivateField("server", server);
            setPrivateField("alertUtils", alert);
            setPrivateField("websocket", websocket);
        } catch (RuntimeException e) {
            fail();
        }

        sut.deleteBoard(board);
        verify(alert, never()).alertError(anyString());
        verify(websocket, never()).deleteBoard(board);
    }

    @Test
    void deleteBoardSuccess() {
        var board = Mockito.mock(Board.class);
        var server = Mockito.mock(ServerUtils.class);
        var alert = Mockito.mock(AlertUtils.class);
        var websocket = Mockito.mock(WebsocketUtils.class);

        Long defaultID = 512452151L;

        when(board.getId()).thenReturn(0L);
        when(server.getDefaultId()).thenReturn(defaultID);
        when(alert.confirmDeletion(anyString())).thenReturn(true);

        List<Board> boardListWithDeleted = new ArrayList<>();
        List<Board> boardListNoDeleted = new ArrayList<>();
        Board nonDeletedBoard = Mockito.mock(Board.class);
        boardListWithDeleted.add(board);
        boardListWithDeleted.add(nonDeletedBoard);
        boardListNoDeleted.add(nonDeletedBoard);

        List<User> users = new ArrayList<>();

        User user1 = Mockito.mock(User.class);
        when(user1.getBoards()).thenReturn(boardListWithDeleted);
        users.add(user1);

        User user2 = Mockito.mock(User.class);
        when(user2.getBoards()).thenReturn(boardListWithDeleted);
        users.add(user2);

        User user3 = Mockito.mock(User.class);
        when(user3.getBoards()).thenReturn(boardListNoDeleted);
        users.add(user3);

        when(server.getAllUsers()).thenReturn(users);

        doAnswer((Answer<Void>) invocation -> {
            User u = invocation.getArgument(0);
            if (u.getBoards().contains(board)) {
                throw new Throwable("User contains board");
            }
            return null;
        }).when(websocket).saveUser(any(User.class));

        try {
            setPrivateField("server", server);
            setPrivateField("alertUtils", alert);
            setPrivateField("websocket", websocket);
        } catch (RuntimeException e) {
            fail();
        }

        try {
            sut.deleteBoard(board);
        } catch (Throwable e) {
            fail("Saving user with a deleted board");
        }
        verify(alert, never()).alertError(anyString());
        verify(alert).confirmDeletion(anyString());
        verify(websocket).deleteBoard(board);
    }
}