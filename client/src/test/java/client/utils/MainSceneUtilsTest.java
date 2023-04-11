package client.utils;

import client.scenes.BoardCtrl;
import client.scenes.MainCtrlTalio;
import client.scenes.TaskListCtrl;
import commons.Board;
import commons.TaskList;
import commons.User;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainSceneUtilsTest {
    @Mock
    private MainCtrlTalio mainCtrl;
    @Mock
    private ServerUtils server;
    @Mock
    private AlertUtils alertUtils;
    @Mock
    private WebsocketUtils websocket;
    @Mock
    private ChildrenManager<TaskList, TaskListCtrl> taskListChildrenManager;
    @Mock
    private ChildrenManager<Board, BoardCtrl> boardListChildrenManager;
    @Mock
    private EntityWebsocketManager<User> joinedBoardsWebsocket;
    @Mock
    private ParentWebsocketManager<TaskList, TaskListCtrl> taskListsWebsocket;
    @Mock
    private EntityWebsocketManager<Board> entityWebsocket;
    @Mock
    private User u;

    private MainSceneUtils sut;
    private HBox hBox;
    private VBox vBox;

    private Board activeBoard;

    @BeforeEach
    public void setup() {
        mainCtrl = Mockito.mock(MainCtrlTalio.class);
        server = Mockito.mock(ServerUtils.class);
        alertUtils = Mockito.mock(AlertUtils.class);
        websocket = Mockito.mock(WebsocketUtils.class);
        taskListChildrenManager = Mockito.mock(ChildrenManager.class);
        boardListChildrenManager = Mockito.mock(ChildrenManager.class);
        joinedBoardsWebsocket = Mockito.mock(EntityWebsocketManager.class);
        taskListsWebsocket = Mockito.mock(ParentWebsocketManager.class);
        entityWebsocket = Mockito.mock(EntityWebsocketManager.class);
        u = Mockito.mock(User.class);

        when(u.getId()).thenReturn(100L);
        when(server.checkUser()).thenReturn(u);
        when(mainCtrl.getUser()).thenReturn(u);

        sut = new MainSceneUtils(server, mainCtrl, alertUtils, websocket);
        hBox = new HBox();
        vBox = new VBox();
        activeBoard = new Board();
        activeBoard.setId(100L);

        sut.setActiveBoard(activeBoard);
        sut.setTaskListChildrenManager(taskListChildrenManager);
        sut.setEntityWebsocket(entityWebsocket);
        sut.setTaskListsWebsocket(taskListsWebsocket);
        sut.setJoinedBoardsWebsocket(joinedBoardsWebsocket);
        sut.setBoardListChildrenManager(boardListChildrenManager);


    }

    @Test
    void checkConstructor() {
        assertEquals(server, sut.getServer());
        assertEquals(websocket, sut.getWebsocket());
    }
    @Test
    void initialize() {
        sut.initialize(vBox, hBox);

        assertEquals(hBox,
                sut.getListChildrenManager().getChildrenContainer());

        assertEquals(vBox,
                sut.getBoardListChildrenManager().getChildrenContainer());
    }

    @Test
    void changeServer() {

        //validateUser() setup
        when(server.getDefaultId()).thenReturn(100L);
        doNothing().when(joinedBoardsWebsocket)
                .register(anyLong(), anyString());
        doNothing().when(mainCtrl).setUser(any());

        //setEntity() setup
        doNothing().when(taskListChildrenManager).updateChildren(any());
        doNothing().when(entityWebsocket).register(anyLong(), anyString());
        doNothing().when(taskListsWebsocket).register(anyLong());

        when(server.getDefaultBoard()).thenReturn(activeBoard);

        sut.changeServer();
    }

    @Test
    void getAndSetActiveBoard() {
        assertEquals(activeBoard, sut.getActiveBoard());
    }

    @Test
    void getAndSetJoinedBoardsWebsocket() {
        assertEquals(joinedBoardsWebsocket, sut.getJoinedBoardsWebsocket());
    }

    @Test
    void setEntity() {
        doNothing().when(taskListChildrenManager).updateChildren(any());
        doNothing().when(entityWebsocket).register(anyLong(), anyString());
        doNothing().when(taskListsWebsocket).register(anyLong());

        sut.setEntity(activeBoard);

        verify(taskListChildrenManager, times(1)).updateChildren(any());
        verify(entityWebsocket, times(1))
                .register(activeBoard.getId(), "update");
        verify(taskListsWebsocket, times(1)).register(activeBoard.getId());
        verify(websocket, times(1))
                .registerForMessages(any(), any(), any());

    }

    @Test
    void back() {
        doNothing().when(server).resetServer();
        doNothing().when(mainCtrl).showConnect();
        sut.back();
        verify(server, times(1)).resetServer();
        verify(mainCtrl, times(1)).showConnect();
    }

    @Test
    void updateJoinedBoards() {
        doNothing().when(boardListChildrenManager).updateChildren(any());
        sut.updateJoinedBoards(u);
        verify(boardListChildrenManager, times(2)).updateChildren(any());

    }

    @Test
    void addBoard() {
        doNothing().when(mainCtrl).showAddBoard();
        sut.addBoard();
        verify(mainCtrl, times(1)).showAddBoard();
    }

    @Test
    void renameBoard() {
        doNothing().when(mainCtrl).showRenameBoard();
        sut.renameBoard();
        verify(mainCtrl, times(1)).showRenameBoard();
    }

    @Test
    void renameBoardFailed() {
        sut.setDefaultBoardID(100L);
        doNothing().when(alertUtils).alertError(any());
        sut.renameBoard();
        verify(alertUtils, times(1))
                .alertError("You cannot rename the default board!");
    }

    @Test
    void removeBoard() {
        doNothing().when(mainCtrl).deleteBoard(any());
        sut.removeBoard();
        verify(mainCtrl, times(1)).deleteBoard(activeBoard);
    }

    @Test
    void copyBoardCode() {
        assertEquals(activeBoard.getCode(), sut.copyBoardCode());
    }

    @Test
    void addList() {
        doNothing().when(mainCtrl).showAddList();
        sut.addList();
        verify(mainCtrl, times(1)).showAddList();
    }

    @Test
    void joinBoard() {
        doNothing().when(mainCtrl).showJoinBoard();
        sut.joinBoard();
        verify(mainCtrl, times(1)).showJoinBoard();
    }

    @Test
    void validateUser() {
        doNothing().when(joinedBoardsWebsocket)
                .register(anyLong(), anyString());
        doNothing().when(mainCtrl).setUser(any());

        sut.validateUser();
        verify(server, times(1)).checkUser();
        verify(mainCtrl, times(1)).setUser(u);
        verify(joinedBoardsWebsocket, times(1)).register(u.getId(), "save");
    }

    @Test
    void adminPassword() {
        when(mainCtrl.isAdmin()).thenReturn(true);
        when(alertUtils.confirmRevertAdmin()).thenReturn(true);
        doNothing().when(mainCtrl).showMain();
        sut.adminPassword();
        verify(mainCtrl, times(1)).setAdmin(false);
        verify(mainCtrl, times(1)).setUser(any());
        verify(mainCtrl, times(1)).showMain();
    }

    @Test
    void adminPasswordFailed() {
        when(mainCtrl.isAdmin()).thenReturn(false);
        doNothing().when(mainCtrl).showAdmin();
        sut.adminPassword();
        verify(mainCtrl, times(1)).showAdmin();
    }

    @Test
    void adminBoards() {
        doNothing().when(boardListChildrenManager).updateChildren(any());
        sut.adminBoards();
        verify(boardListChildrenManager, times(2)).updateChildren(any());
    }
}