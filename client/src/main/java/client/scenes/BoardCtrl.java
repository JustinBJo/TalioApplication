package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.awt.*;
import java.io.IOException;

public class BoardCtrl implements Callback<ListView<Board>, ListCell<Board>> {

    private final MainCtrlTalio mainCtrl;
    private static MainCtrlTalio mainCtrlCopy;

    private final ServerUtils server;
    private static ServerUtils serverCopy;

    private final MainSceneCtrl mainSceneCtrl;
    private static MainSceneCtrl mainSceneCtrlCopy;


    private Board board;

    /**
     * Constructor with no parameters for the Board class
     * One of the FXML loader methods calls this when loading
     * the FXML view of this object.
     * Sets values to null so in case they have already been assigned
     * values, they are assigned their static copies
     */
    public BoardCtrl() {
        if (serverCopy != null) {
            this.server = serverCopy;
            this.mainCtrl = mainCtrlCopy;
            this.mainSceneCtrl = mainSceneCtrlCopy;
        }
        else  {
            this.mainCtrl = null;
            this.server = null;
            this.mainSceneCtrl = null;
        }
    }

    /**
     * Main constructor for the board class
     * @param mainCtrl inject the main controller used
     * @param server inject the server used
     * @param mainSceneCtrl inject the main scene controller used
     */
    @Inject
    public BoardCtrl(MainCtrlTalio mainCtrl, ServerUtils server,
                     MainSceneCtrl mainSceneCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.mainSceneCtrl = mainSceneCtrl;

        serverCopy = server;
        mainSceneCtrlCopy = mainSceneCtrl;
        mainCtrlCopy = mainCtrl;

        FXMLLoader fxmlLoader = new FXMLLoader((getClass()
                .getResource("Board.fxml")));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public void setBoard(Board board) {
        this.board = board;
        if (board.getTitle() == null) {
            board.setTitle("Untitled");
        }
        boardName.setText(board.getTitle());
    }

    /**
     * Used to load the FXML view of the set Board object into the main scene
     * @param param The ListView of Boards where the object will be rendered
     * @return a ListCell representation of the parameter given through param
     */
    @Override
    public ListCell<Board> call(ListView<Board> param) {
        return new ListCell<Board>() {
            private BoardCtrl controller;
            private FXMLLoader loader;

            {
                loader = new FXMLLoader(getClass()
                        .getResource("Board.fxml"));
                try {
                    loader.load();
                    controller = loader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void updateItem(Board board, boolean empty) {
                super.updateItem(board, empty);
                if (empty || board == null) {
                    // Clear the cell content if there is no item to display
                    setText(null);
                    setGraphic(null);
                } else {
                    controller.setBoard(board);
                    setGraphic(controller.root);
                }
            }
        };
    }

    /**
     * Leave method associated to the "X" button in the FXML view of the board
     * Allows the user to leave a board
     */
    public void leave() {
        Board copy = board;
       mainCtrl.mainSceneCtrl.boards.getItems().remove(copy);
       mainCtrl.mainSceneCtrl.boardData.remove(copy);
       mainCtrl.getUser().removeBoard(copy);
       server.saveUser(mainCtrl.getUser());
    }
}
