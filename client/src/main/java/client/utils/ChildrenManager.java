package client.utils;

import client.scenes.IEntityRepresentation;
import commons.IEntity;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChildrenManager
        <T extends IEntity, C extends IEntityRepresentation<T>> {
    private final Pane childrenContainer;
    private final Map<T, Pair<C, Parent>> childUIMap;
    private final Class<C> childSceneCtrl;
    private final String childFxmlFileName;

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
                childrenContainer.getChildren()
                    .add(loadedChild.getValue());
            } else {
                childrenContainer.getChildren()
                    .set(insertAtIndex, loadedChild.getValue());
            }
            // Initialize its controller with this task list
            loadedChild.getKey().setEntity(child);
            // Add its reference to the map
            childUIMap.put(child, loadedChild);
        }

        // Remove UI elements for removed children
        for (T child : changedChildren) {
            // Remove UI element from its container
            // and child from map at the same time
            var uiElement = childUIMap.remove(child).getValue();
            childrenContainer.getChildren().remove(uiElement);
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
}
