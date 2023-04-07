package client.utils;

import client.scenes.MainCtrlTalio;
import com.google.inject.Inject;
import commons.Board;
import jakarta.ws.rs.WebApplicationException;

public class JoinBoardUtils {

    private final MainCtrlTalio mainCtrl;
    private final WebsocketUtils websocket;
    private final ServerUtils server;
    private final AlertUtils alertUtils;

    @Inject
    public JoinBoardUtils(MainCtrlTalio mainCtrl,
                          WebsocketUtils websocket,
                          ServerUtils server,
                          AlertUtils alertUtils) {
        this.mainCtrl = mainCtrl;
        this.websocket = websocket;
        this.server = server;
        this.alertUtils = alertUtils;
    }

    public void cancel() {
        mainCtrl.showMain();
    }

    public void join(String boardCode) {
        boolean added;

        try {
            Board b = server.getBoardByCode(boardCode);
            if (mainCtrl.getUser().getBoards().contains(b)) {
                mainCtrl.setActiveBoard(b);
                mainCtrl.showMain();
                return;
            }
            added = addBoard(b);


            if (added) {
                mainCtrl.setActiveBoard(b);
                websocket.saveUser(mainCtrl.getUser());
            }
            else {
                alertUtils.alertError("There is no board with this code!");
            }
            mainCtrl.showMain();
        } catch (WebApplicationException e) {
            alertUtils.alertError("There is no board with this code!");
        }
    }

    public boolean addBoard(Board board) {
        boolean added = false;
        long id = board.getId();
        if (server.getBoardById(id) != null) {
            server.addBoard(board);
            mainCtrl.getUser().addBoard(board);
            added = true;
        }
        return added;
    }

    public MainCtrlTalio getMainCtrl() {
        return mainCtrl;
    }

    public WebsocketUtils getWebsocket() {
        return websocket;
    }

    public ServerUtils getServer() {
        return server;
    }

    public AlertUtils getAlertUtils() {
        return alertUtils;
    }
}
