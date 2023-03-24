package client.utils;

import client.scenes.IEntityRepresentation;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChildrenManager<T> {
    private final Pane childrenContainer;
    private final Map<T, Parent> childUIMap;
    private final Class<? extends IEntityRepresentation<T>> childSceneCtrl;
    private final String childFxmlFileName;

    public ChildrenManager(
            Pane childrenContainer,
            Class<? extends IEntityRepresentation<T>> childSceneCtrl,
            String childFxmlFileName) {
        this.childrenContainer = childrenContainer;
        this.childSceneCtrl = childSceneCtrl;
        this.childFxmlFileName = childFxmlFileName;
        this.childUIMap = new HashMap<>();
    }

    public void updateChildren(List<T> children) {
        // Create UI elements for new children
        for (T child: children) {
            boolean hasUIElement = childUIMap.containsKey(child);

            if (hasUIElement) { continue; }

            // Instantiate child UI element
            var loadedChild = BuildUtils.loadFXML(childSceneCtrl, childFxmlFileName);
            // Add it to its container
            childrenContainer.getChildren().add(loadedChild.getValue());
            // Initialize its controller with this task list
            loadedChild.getKey().setEntity(child);
            // Add its reference to the map
            childUIMap.put(child, loadedChild.getValue());
        }

        // Remove UI elements for removed task lists
        for (T child: childUIMap.keySet()) {
            boolean existsInUpdatedList = children.contains(child);

            if (existsInUpdatedList) { continue; }

            // Remove UI element from its parent container and from task list from map at the same time
            childrenContainer.getChildren().remove(childUIMap.remove(child));
        }
    }
}
