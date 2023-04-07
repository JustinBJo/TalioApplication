package client.scenes;


import client.utils.AlertUtils;
import client.utils.BoardUtils;
import client.utils.ServerUtils;
import client.utils.WebsocketUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


public class BoardCtrl implements IEntityRepresentation<Board> {

   private final BoardUtils utils;


    /**
     * Main constructor for the board class
     *
     * @param mainCtrl  inject the main controller used
     * @param server    inject the server used
     */
    @Inject
    public BoardCtrl(BoardUtils utils) {
        this.utils = utils;
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
        utils.setEntity(board);
        boardName.setText(board.getTitle());
    }

    /**
     * Leave method associated to the "X" button in the FXML view of the board
     * Allows the user to leave a board
     */
    public void leave() {
       utils.leave();
    }

    /**
     * accesses the chosen board
     */
    public void access() {
        utils.access();
    }
}
