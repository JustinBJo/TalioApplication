package client.scenes;


import client.utils.AlertUtils;
import client.utils.ServerUtils;
import client.utils.WebsocketUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


public class BoardCtrl implements IEntityRepresentation<Board> {

    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;
    private final WebsocketUtils websocket;
    private final AlertUtils alertUtils;
    private Board board;


    /**
     * Main constructor for the board class
     *
     * @param mainCtrl  inject the main controller used
     * @param server    inject the server used
     */
    @Inject
    public BoardCtrl(MainCtrlTalio mainCtrl,
                     ServerUtils server,
                     WebsocketUtils websocket,
                     AlertUtils alertUtils) {
        this.websocket = websocket;
        this.alertUtils = alertUtils;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @FXML
    Label boardName;

    @FXML
    Button access;

    @FXML
    Button leave;

    @FXML
    AnchorPane root;

    /**
     * Sets the board object associated with this FXML view
     * @param board the board object corresponding to the fxml
     */
    public void setEntity(Board board) {
        this.board = board;
        if (board.getTitle() == null) {
            board.setTitle("Untitled");
        }
        boardName.setText(board.getTitle());
    }

    /**
     * Leave method associated to the "X" button in the FXML view of the board
     * Allows the user to leave a board
     */
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

    /**
     * accesses the chosen board
     */
    public void access() {
        mainCtrl.setActiveBoard(board);
    }
}
