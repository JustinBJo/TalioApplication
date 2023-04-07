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

    /**
     * Constructor for the JoinBoard service
     * @param mainCtrl the mainCtrl to be injected
     * @param websocket the websocket to be injected
     * @param server the server to be injected
     * @param alertUtils the alertUtils to be injected
     */
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

    /**
     * logic for the cancel button
     */
    public void cancel() {
        mainCtrl.showMain();
    }

    /**
     * adds the given board to the user's boardlist or switches
     * to it if it has already been joined
     * @param boardCode the code of the board to be joined
     */
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

    /**
     * adds the board to the database if does not yet exist
     * @param board the board to be added
     * @return whether it has been added or not
     */
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

    /**
     * getter for the mainCtrl
     * @return the mainCtrl
     */
    public MainCtrlTalio getMainCtrl() {
        return mainCtrl;
    }

    /**
     * getter for the websocket
     * @return the websocket
     */
    public WebsocketUtils getWebsocket() {
        return websocket;
    }

    /**
     * getter for the Server
     * @return the server
     */
    public ServerUtils getServer() {
        return server;
    }

    /**
     * getter for the AlertUtils
     * @return the alertUtils
     */
    public AlertUtils getAlertUtils() {
        return alertUtils;
    }
}
