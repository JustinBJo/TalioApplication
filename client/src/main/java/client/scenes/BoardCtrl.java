package client.scenes;


import client.utils.ErrorUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


public class BoardCtrl implements IEntityRepresentation<Board> {

    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;
    private Board board;


    /**
     * Main constructor for the board class
     * @param mainCtrl inject the main controller used
     * @param server inject the server used
     */
    @Inject
    public BoardCtrl(MainCtrlTalio mainCtrl, ServerUtils server) {
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
        if (board.getId() != server.getDefaultId()) {
        mainCtrl.getUser().removeBoard(board);
        server.saveUser(mainCtrl.getUser());
        board = null;
        mainCtrl.refreshBoard(); }
        else
            ErrorUtils.alertError("You cannot leave the default board!");
    }

    /**
     * accesses the chosen board
     */
    public void access() {
        mainCtrl.setActiveBoard(board);
        mainCtrl.refreshBoard();
    }
}
