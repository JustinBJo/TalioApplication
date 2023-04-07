package client.scenes;

import client.utils.AlertUtils;
import client.utils.JoinBoardUtils;
import client.utils.ServerUtils;
import client.utils.WebsocketUtils;
import com.google.inject.Inject;
import commons.Board;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class JoinBoardCtrl {

    private final JoinBoardUtils utils;

    /**
     * Injector constructor
     *
     * @param mainCtrl  the main controller
     * @param server    the server used
     */
    @Inject
    public JoinBoardCtrl(JoinBoardUtils utils) {
       this.utils = utils;
    }

    @FXML
    TextField code;

    /**
     * Resets the fields and returns to the main scene
     */
    public void cancel() {
        code.clear();
        utils.cancel();
    }

    /**
     * Adds the Board to the joined boards of the
     * user and displays it on the main scene
     * if code does not exist, prompts an error
     */
    public void join() {
        String boardCode = code.getText();
        code.clear();
        utils.join(boardCode);

    }

    /**
     * adds a board to the database and the user
     * @param board the board to be added
     * @return whether the board has been added
     */
    public boolean addBoard(Board board) {
        return utils.addBoard(board);
    }
}
