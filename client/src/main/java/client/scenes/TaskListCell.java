package client.scenes;

import commons.TaskList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class TaskListCell extends ListCell<TaskList> {

    private final MainSceneCtrl mainSceneCtrl;
    private TaskListCtrl controller;
    private FXMLLoader loader;

    /**
     * Constructor for TaskListCell
     * @param taskListCtrl the taskListCtrl that this cell holds
     * @param mainSceneCtrl the parent scene
     */
    public TaskListCell(TaskListCtrl taskListCtrl,
                        MainSceneCtrl mainSceneCtrl) {
        this.mainSceneCtrl = mainSceneCtrl;
        loader = new FXMLLoader(getClass().getResource("TaskList.fxml"));
        try {
            loader.load();
            controller = loader.getController();
            mainSceneCtrl.taskListCtrls.add(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the TaskListCtrl that this entity holds
     * @return the controller
     */
    public TaskListCtrl getController() {
        return controller;
    }

    @Override
    protected void updateItem(TaskList taskList, boolean empty) {
        super.updateItem(taskList, empty);
        if (empty || taskList == null) {
            // Clear the cell content if there is no item to display
            setText(null);
            setGraphic(null);
        } else {
            controller.setTaskList(taskList);
            setGraphic(controller.root);
        }
    }
}
