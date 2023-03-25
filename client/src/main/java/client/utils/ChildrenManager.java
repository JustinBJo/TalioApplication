package client.utils;

import client.scenes.IEntityRepresentation;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChildrenManager<T, TCtrl extends IEntityRepresentation<T>> {
    private final Pane childrenContainer;
    private final Map<T, Pair<TCtrl, Parent>> childUIMap;
    private final Class<TCtrl> childSceneCtrl;
    private final String childFxmlFileName;

    public ChildrenManager(
            Pane childrenContainer,
            Class<TCtrl> childSceneCtrl,
            String childFxmlFileName) {
        this.childrenContainer = childrenContainer;
        this.childSceneCtrl = childSceneCtrl;
        this.childFxmlFileName = childFxmlFileName;
        this.childUIMap = new HashMap<>();
    }

    public void updateChildren(List<T> children) {
        // Remove UI elements for removed task lists
        List<T> toBeRemoved = new ArrayList<>();
        for (T child: childUIMap.keySet()) {
            boolean existsInUpdatedList = children.contains(child);
            // Tag child to be removed later. Not done here because removing
            // from a list you're iterating over causes errors.
            if (!existsInUpdatedList) { toBeRemoved.add(child); }
        }
        for (T child: toBeRemoved) {
            // Remove UI element from its parent container and from task list from map at the same time
            childrenContainer.getChildren().remove(childUIMap.remove(child).getValue());
        }

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
            childUIMap.put(child, loadedChild);
        }
    }

    public List<TCtrl> getChildrenCtrls() {
        List<TCtrl> ctrlList = new ArrayList<>();
        for (var ctrlAndParent: childUIMap.values()) {
            ctrlList.add(ctrlAndParent.getKey());
        }
        return ctrlList;
    }
}
