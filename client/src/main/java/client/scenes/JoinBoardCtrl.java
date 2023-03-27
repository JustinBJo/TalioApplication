package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class JoinBoardCtrl {

    private final MainCtrlTalio mainCtrl;
    private final ServerUtils server;

    /**
     * Injector constructor
     * @param mainCtrl the main controller
     * @param server the server used
     */
    @Inject
    public JoinBoardCtrl(MainCtrlTalio mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @FXML
    TextField code;

    /**
     * Resets the fields and returns to the main scene
     */
    public void cancel() {
        code.clear();
        mainCtrl.showMain();
        mainCtrl.getMainSceneCtrl().refresh();
    }

    /**
     * Adds the Board to the joined boards of the
     * user and displays it on the main scene
     * if code does not exist, prompts an error
     */
    public void join() {
        String boardCode = code.getText();
        boolean added;
        try {
            Board b = server.getBoardByCode(boardCode);
            if (mainCtrl.getUser().getBoards().contains(b)) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("This board has already been joined!");
                alert.showAndWait();
                return;
            }
            added = addBoard(b);


            if (added) {
                mainCtrl.setActiveBoard(b);
                server.saveUser(mainCtrl.getUser());
            }
            else {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("There is no board with this code!");
                alert.showAndWait();
            }
            code.clear();
            mainCtrl.showMain();
            mainCtrl.getMainSceneCtrl().refresh();
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("The board with this code does not exist!");
            alert.showAndWait();
        }

    }

    /**
     * adds a board to the database and the user
     * @param board the board to be added
     * @return whether the board has been added
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
}
