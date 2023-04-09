package client.scenes;

import client.utils.*;
import com.google.inject.Inject;
import commons.Board;
import commons.User;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.util.*;

public class MainSceneCtrl implements IEntityRepresentation<Board>  {


    private final MainSceneUtils utils;
    @FXML
    Label sceneTitle;
    @FXML
    VBox boardsContainer;
    @FXML
    HBox taskListsContainer;

    @FXML
    MenuItem createBoardMenu;
    @FXML
    MenuItem renameBoardMenu;
    @FXML
    MenuItem deleteBoardMenu;
    @FXML
    MenuItem joinBoardMenu;
    @FXML
    MenuItem joinServerMenu;

    @FXML
    ImageView menuIcon;
    @FXML
    ImageView adminIcon;
    @FXML
    ImageView copyIcon;

    @FXML
    Label boardCode;
    @FXML
    Label serverAddr;

    /**
     * constructor
     */
    @Inject
    public MainSceneCtrl(MainSceneUtils utils) {
        this.utils = utils;
    }


    /**
     * This is called only once by the FXML builder,
     * after FXML components are initialized.
     */
    public void initialize() {
        utils.initialize(boardsContainer, taskListsContainer);

        // Set button icons
        Image menu = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/menuicon.png")));
        menuIcon.setImage(menu);

        Image admin = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/adminicon.png")));
        adminIcon.setImage(admin);

        Image copy = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/client/images/copyicon.png")));
        copyIcon.setImage(copy);
    }

    /**
     * Handles a server change
     */
    public void changeServer() {
        utils.changeServer();
    }

    /**
     * @return board currently shown in scene
     */
    public Board getActiveBoard() {
        return utils.getActiveBoard();
    }

    /**
     * Sets current active board and updates the main scene accordingly
     * @param activeBoard new active board
     */
    public void setEntity(Board activeBoard) {
        utils.setEntity(activeBoard);

        Platform.runLater(() -> {
            sceneTitle.setText(activeBoard.getTitle());
            boardCode.setText(activeBoard.getCode());
        });
    }

    /**
     * go back to the connect screen
     * TODO: delete all the potential local storage,
     * since the user want to connect to a different server
     */
    public void back() {
        utils.back();
    }

    /**
     * Refresh the view, showing all task lists
     */
    public void updateJoinedBoards(User user) {
        utils.updateJoinedBoards(user);
    }

    /**
     * add a board to the list
     */
    public void addBoard() {
        utils.addBoard();
    }

    /**
     * Rename the current board
     */
    public void renameBoard() {
        utils.renameBoard();
    }

    /**
     * Delete the active board
     * After deleting, go back to the connect screen
     * Behaviour after deletion can be changed in future implementations
     */
    public void removeBoard() {
        utils.removeBoard();
    }

    /**
     * Copies the code of current board
     * If the active board is null i.e. this is the default board,
     * then it copies an empty string
     */
    public void copyBoardCode() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        String code = utils.copyBoardCode();
        content.putString(code);
        clipboard.setContent(content);

        System.out.println("The code for this board is copied!");
        System.out.println("Code: " + code);
    }

    /**
     * set the server address to be displayed
     * @param address the address to be displayed
     */
    public void setServerAddr(String address) {
        this.serverAddr.setText(address);
    }

    /**
     * add a list to the list
     */
    public void addList() {
        utils.addList();
    }

    /**
     * displays the join board scene
     */
    public void joinBoard() {
        utils.joinBoard();
    }

    /**
     * Handles setting the current user
     */
    public void validateUser() {
        utils.validateUser();
    }

    /**
     * functionality for the "admin" icon button
     */
    public void adminPassword() {
        utils.adminPassword();
    }
}

