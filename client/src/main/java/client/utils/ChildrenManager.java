package client.utils;

import client.scenes.IEntityRepresentation;
import commons.IEntity;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ChildrenManager
        <T extends IEntity, C extends IEntityRepresentation<T>> {
    private final Pane childrenContainer;
    private final Map<T, Pair<C, Parent>> childUIMap;
    private final Class<C> childSceneCtrl;
    private final String childFxmlFileName;
    private Consumer<C> updatedChildConsumer;

    /**
     * @param childrenContainer JavaFX pane which contains the children
     * @param childSceneCtrl Class of child's scene controller
     * @param childFxmlFileName Name of FXML file which defines child's scene
     */
    public ChildrenManager(
            Pane childrenContainer,
            Class<C> childSceneCtrl,
            String childFxmlFileName) {
        this.childrenContainer = childrenContainer;
        this.childSceneCtrl = childSceneCtrl;
        this.childFxmlFileName = childFxmlFileName;
        this.childUIMap = new HashMap<>();
    }

    /**
     * Removes a single child scene
     * @param child to be removed
     */
    public void removeChild(T child) {
        List<T> children = new ArrayList<>(childUIMap.keySet());
        T forRemoval = null;
        for (T current : children) {
            if (current.getId().equals(child.getId())) {
                forRemoval = current;
                break;
            }
        }

        if (children.remove(forRemoval)) {
            updateChildren(children);
        }
    }

    /**
     * Adds or updates a single child scene
     * @param child to be added or updated
     * @return the child's scene controller
     */
    public C addOrUpdateChild(T child) {
        List<T> children = new ArrayList<>(childUIMap.keySet());

        boolean updated = false;

        for (T currentChild : childUIMap.keySet()) {
            if (child.getId().equals(currentChild.getId())) {
                children.remove(currentChild);
                children.add(child);
                updated = true;
                break;
            }
        }

        if (!updated) {
            children.add(child);
        }

        updateChildren(children);
        return childUIMap.get(child).getKey();
    }

    /**
     * Removes all children
     */
    public void clear() {
        childUIMap.clear();
        childrenContainer.getChildren().clear();
    }

    /**
     * Adds or removes children's scenes based on new list of children
     * @param children list of updated children
     */
    public void updateChildren(List<T> children) {
        // Tag child to be removed later
        List<T> changedChildren = new ArrayList<>();
        for (T child : childUIMap.keySet()) {
            boolean existsInUpdatedList = children.contains(child);
            if (!existsInUpdatedList) changedChildren.add(child);
        }

        // Create UI elements for new/updated children
        for (T child : children) {
            boolean hasUIElement = childUIMap.containsKey(child);

            if (hasUIElement) continue; // Not updated/new

            // If updated (not new), find old index
            int insertAtIndex = -1;
            for (T changed : changedChildren) {
                if (changed.getId().equals(child.getId())) {
                    var changedUi = childUIMap.get(changed).getValue();
                    insertAtIndex =
                            childrenContainer.getChildren().indexOf(changedUi);
                }
            }

            // Instantiate child UI element
            var loadedChild =
                    BuildUtils.loadFXML(childSceneCtrl, childFxmlFileName);

            // Add it to its container
            if (insertAtIndex < 0) {
                Platform.runLater(() -> {
                    childrenContainer.getChildren()
                            .add(loadedChild.getValue());
                });
            } else {
                int finalInsertAtIndex = insertAtIndex;
                Platform.runLater(() -> {
                    childrenContainer.getChildren()
                        .set(finalInsertAtIndex, loadedChild.getValue());
                });
            }
            // Initialize its controller with this task list
            loadedChild.getKey().setEntity(child);
            // Do any additional actions
            if (updatedChildConsumer != null) {
                updatedChildConsumer.accept(loadedChild.getKey());
            }
            // Add its reference to the map
            childUIMap.put(child, loadedChild);
        }

        // Remove UI elements for removed children
        for (T child : changedChildren) {
            // Remove UI element from its container
            // and child from map at the same time
            var uiElement = childUIMap.remove(child).getValue();
            Platform.runLater(() -> {
                childrenContainer.getChildren().remove(uiElement);
            });
        }
    }

    /**
     * @return list of scene controllers for all current children
     */
    public List<C> getChildrenCtrls() {
        List<C> ctrlList = new ArrayList<>();
        for (var ctrlAndParent : childUIMap.values()) {
            ctrlList.add(ctrlAndParent.getKey());
        }
        return ctrlList;
    }

    /**
     * Defines a consumer that will be called for every updated child
     * @param updatedChildConsumer consumer
     */
    public void setUpdatedChildConsumer(Consumer<C> updatedChildConsumer) {
        this.updatedChildConsumer = updatedChildConsumer;
    }

    /**
     * getter for the child container
     * @return the child container
     */
    public Pane getChildrenContainer() {
        return childrenContainer;
    }

    /**
     * getter for the child Controller
     * @return the child scene controller
     */
    public Class<C> getChildSceneCtrl() {
        return childSceneCtrl;
    }

    /**
     * getter for the FXML name
     * @return the FXML name as string
     */
    public String getChildFxmlFileName() {
        return childFxmlFileName;
    }

    /**
     * getter for the child UI map
     * @return the Map of ChildUI
     */
    public Map<T, Pair<C, Parent>> getChildUIMap() {
        return childUIMap;
    }
}
