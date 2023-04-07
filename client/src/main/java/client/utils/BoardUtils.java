package client.utils;

import client.scenes.MainCtrlTalio;
import com.google.inject.Inject;
import commons.Board;

public class BoardUtils {
    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;
    private final WebsocketUtils websocket;
    private final AlertUtils alertUtils;

    private Board board;

    @Inject
    public BoardUtils(MainCtrlTalio mainCtrl,
                      ServerUtils server,
                      WebsocketUtils websocket,
                      AlertUtils alertUtils) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.websocket = websocket;
        this.alertUtils = alertUtils;
    }

    public void setEntity(Board board) {
        this.board = board;
        if (board.getTitle() == null) {
            board.setTitle("Untitled");
        }
    }

    public void leave() {
        if (board.getId() == server.getDefaultId()) {
            alertUtils.alertError("You cannot leave the default board!");
            return;
        }

        if (!mainCtrl.isAdmin()) {
            mainCtrl.getUser().removeBoard(board);
            websocket.saveUser(mainCtrl.getUser());
            board = null;
            mainCtrl.setActiveBoard(server.getDefaultBoard());
            return;
        }

        mainCtrl.deleteBoard(board);
        board = null;
    }

    public void access() {
        mainCtrl.setActiveBoard(board);
    }

    public MainCtrlTalio getMainCtrl() {
        return mainCtrl;
    }

    public ServerUtils getServer() {
        return server;
    }

    public WebsocketUtils getWebsocket() {
        return websocket;
    }

    public AlertUtils getAlertUtils() {
        return alertUtils;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
