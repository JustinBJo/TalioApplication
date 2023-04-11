package client.utils;

import com.google.inject.Inject;
import commons.Board;

public class BoardUtils {
    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;
    private final WebsocketUtils websocket;
    private final AlertUtils alertUtils;

    private Board board;

    /**
     * Constructor for the BoardUtils service
     * @param mainCtrl the mainCtrl injected
     * @param server the server injected
     * @param websocket the websocket injected
     * @param alertUtils the alertUtils injected
     */
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

    /**
     * sets the current board Entity
     * @param board the board to be set
     */
    public void setEntity(Board board) {
        this.board = board;
        if (board.getTitle() == null) {
            board.setTitle("Untitled");
        }
    }

    /**
     * provides logic for the "X" button
     * Removes the board from overview if pressed by user
     * Deletes the board from repository and al users' lists
     * if pressed by admin
     */
    public void leave() {
        if (board.getId() == server.getDefaultId()) {
            alertUtils.alertError("You cannot leave the default board!");
            return;
        }

        if (!mainCtrl.isAdmin()) {
            mainCtrl.getUser().removeBoard(board);
            websocket.saveUser(mainCtrl.getUser());
            if (board.equals(mainCtrl.getActiveBoard()))
                mainCtrl.setActiveBoard(server.getDefaultBoard());
            board = null;
            return;
        }

        mainCtrl.deleteBoard(board);
        mainCtrl.showAdminBoards();
        board = null;
    }

    /**
     * switches current board to the selected one
     */
    public void access() {
        mainCtrl.setActiveBoard(board);
    }

    /**
     * getter for the mainCtrl
     * @return the mainCtrl
     */
    public MainCtrlTalio getMainCtrl() {
        return mainCtrl;
    }

    /**
     * getter for the server
     * @return the server used
     */
    public ServerUtils getServer() {
        return server;
    }

    /**
     * getter for the websocket
     * @return the websocket used
     */
    public WebsocketUtils getWebsocket() {
        return websocket;
    }

    /**
     * getter for the AlertUtils
     * @return the AlertUtils used
     */
    public AlertUtils getAlertUtils() {
        return alertUtils;
    }

    /**
     * the getter for the Board used
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * setter for the current board
     * @param board the board to be set
     */
    public void setBoard(Board board) {
        this.board = board;
    }
}
