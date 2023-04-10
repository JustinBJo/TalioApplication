package client.scenes;

import client.utils.JoinBoardUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class JoinBoardCtrl {

    private final JoinBoardUtils utils;
    @FXML
    TextField code;

    /**
     * Injector constructor
     * @param utils the service used for logic
     */
    @Inject
    public JoinBoardCtrl(JoinBoardUtils utils) {
       this.utils = utils;
    }

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
