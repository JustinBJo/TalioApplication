package client.scenes;

import client.utils.AddTitledEntityUtils;
import com.google.inject.Inject;
import commons.Subtask;
import commons.Task;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddTitledEntityCtrl {

    public enum Type {
        TaskList,
        Board,
        RenameTaskList,
        RenameBoard,
        Subtask,
        RenameSubtask
    }
    private AddTitledEntityUtils utils;
    private Type type;

    @FXML
    Button cancel;
    @FXML
    Button confirm;
    @FXML
    TextField textField;
    @FXML
    Label header;

    /**
     * constructor
     * @param utils the class utils
     */
    @Inject
    public AddTitledEntityCtrl(AddTitledEntityUtils utils) {
        this.utils = utils;
    }

    /**
     * Initializes the scene for a certain titled entity type
     * @param type which titled entity will this scene add
     */
    public void initialize(Type type) {
        this.type = type;
        utils.setType(type);

        switch (type) {
            case TaskList:
                setHeader("Add new list");
                break;
            case Board:
                setHeader("Add new board");
                break;
            case RenameTaskList:
                setHeader("Edit list");
                break;
            case RenameBoard:
                setHeader("Edit current board");
                break;
            case Subtask:
                setHeader("Add a new subtask");
                break;
            case RenameSubtask:
                setHeader("Edit subtask");
                break;

            // Error handling (very unlikely, as it is an enum)
            default:
                pressCancel();
                break;
        }
    }

    /**
     * @param taskListToEdit task list which will be edited
     *                       if type is RenameTaskList
     */
    public void setTaskListToEdit(TaskList taskListToEdit) {
        utils.setTaskListToEdit(taskListToEdit);
    }

    /**
     * @param subtask subtask which will be edited
     *                if type is RenameSubtask
     */
    public void setSubtaskToEdit(Subtask subtask) {
        utils.setSubtaskToEdit(subtask);
    }

    /**
     * Setter for the current task
     * @param currentTask - the task we are adding the subtask to
     */
    public void setCurrentTask(Task currentTask) {
        utils.setCurrentTask(currentTask);
    }

    /**
     * Sets the text in the scene's header
     * @param text new header text
     */
    private void setHeader(String text) {
        header.setText(text);
    }

    /**
     * Defines the behaviour of pressing the "cancel" button
     */
    public void pressCancel() {
        textField.clear();
        utils.pressCancel();
    }

    /**
     * Defines the behaviour of pressing the "confirm" button
     */
    public void pressConfirm() {
        String title = textField.getText();
        textField.clear();
        utils.pressConfirm(title);
    }
}
