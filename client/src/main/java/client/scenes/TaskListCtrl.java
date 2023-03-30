package client.scenes;

import client.utils.ChildrenManager;
import client.utils.ErrorUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class TaskListCtrl implements IEntityRepresentation<TaskList> {
    @FXML
    AnchorPane root;
    @FXML
    Label title;
    @FXML
    Button deleteList;
    @FXML
    Button addTask;
    @FXML
    Button rename;
    @FXML
    VBox taskContainer;
    @FXML
    ImageView editIcon;

    private final ServerUtils server;
    private final MainCtrlTalio mainCtrl;
    private ChildrenManager<Task, CardCtrl> taskChildrenManager;
    private TaskList taskList;

    /**
     * Constructor with dependency injection
     */
    @Inject
    public TaskListCtrl(ServerUtils server,
                        MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }


    /**
     * Create children manager after FXML components are initialized
     */
    public void initialize() {
        this.taskChildrenManager =
                new ChildrenManager<>(
                    taskContainer,
                    CardCtrl.class,
                    "Card.fxml"
                );

        Image editIcon = new Image(getClass()
                .getResourceAsStream("/client/images/editicon.png"));
        this.editIcon.setImage(editIcon);
    }

    /**
     * Set the TaskList instance that this Scene holds
     * @param taskList the TaskList instance to be set
     */
    public void setEntity(TaskList taskList) {
        this.taskList = taskList;
        if (taskList.getTitle() == null) {
            taskList.setTitle("Untitled");
        }
        title.setText(taskList.getTitle());

        refresh();
    }

    /**
     * Updates this list's tasks
     */
    public void refresh() {
        if (taskList == null) {
            // no children if there's no task list
            taskChildrenManager.updateChildren(new ArrayList<>());
        }
        var tasks = server.getTaskListData(taskList);
        taskChildrenManager.updateChildren(tasks);

        for (CardCtrl cardCtrl :
                taskChildrenManager.getChildrenCtrls()) {
            cardCtrl.setParentList(taskList);
        }
    }

    /**
     * Deletes this task list
     */
    public void delete() {
        boolean confirmation = server.confirmDeletion("list");

        if (confirmation) {
            server.deleteTaskList(taskList);
            taskList = null;
            mainCtrl.refreshBoard();
        }
    }

    /**
     * Switches to the rename scene and refreshes main scene
     */
    public void rename() {
        mainCtrl.showRenameList(taskList);
        mainCtrl.refreshBoard();
    }


    /**
     * add a task to the list
     */
    public void addTask() {
        if (taskList == null) {
            ErrorUtils.alertError("No list to add task to!");
            return;
        }
        mainCtrl.showAddTask(taskList);
    }
}
