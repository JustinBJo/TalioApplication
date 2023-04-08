package client.utils;

import client.scenes.TaskListCtrl;
import commons.TaskList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ChildrenManagerTest extends ApplicationTest {

    private ChildrenManager<TaskList, TaskListCtrl> sut;
    private Pane container;
    private int numConsumerCalls;

    private TaskList[] defaultChildEl;
    private TaskListCtrl[] defaultChildCtrl;
    private Parent[] defaultChildUI;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        ObservableList<Node> containerList =
                FXCollections.observableArrayList();
        container = Mockito.mock(Pane.class);
        when(container.getChildren()).thenReturn(containerList);

        Supplier<Pair<TaskListCtrl, Parent>> supplier = () -> {
            TaskListCtrl ctrl = Mockito.mock(TaskListCtrl.class);
            Parent p = Mockito.mock(Parent.class);
            return new Pair<>(ctrl, p);
        };

        sut = new ChildrenManager<>(container, supplier);

        TaskList taskListA = new TaskList("A");
        taskListA.setId(1L);
        TaskList taskListB = new TaskList("B");
        taskListB.setId(2L);
        defaultChildEl = new TaskList[] {taskListA, taskListB};

        defaultChildCtrl = new TaskListCtrl[] {
                Mockito.mock(TaskListCtrl.class),
                Mockito.mock(TaskListCtrl.class),
        };
        defaultChildUI = new Parent[] {
                Mockito.mock(Parent.class),
                Mockito.mock(Parent.class),
        };

        Map<TaskList, Pair<TaskListCtrl, Parent>> defaultUIMap =
                new HashMap<>();

        for (int i = 0; i < 2; i++) {
            container.getChildren().add(defaultChildUI[i]);
            defaultUIMap.put(
                    defaultChildEl[i],
                    new Pair<>(defaultChildCtrl[i], defaultChildUI[i])
            );
        }

        Field mapField = ChildrenManager.class.getDeclaredField("childUIMap");
        mapField.setAccessible(true);
        mapField.set(sut, defaultUIMap);

        Field consumerField =
                ChildrenManager.class.getDeclaredField("updatedChildConsumer");
        consumerField.setAccessible(true);
        consumerField.set(sut,
                (Consumer<TaskListCtrl>) taskListCtrl -> numConsumerCalls++);

        numConsumerCalls = 0;
    }

    Map<TaskList, Pair<TaskListCtrl, Parent>> getUIMap() {
        Map<TaskList, Pair<TaskListCtrl, Parent>> map = null;
        try {
            Field field = ChildrenManager.class.getDeclaredField("childUIMap");
            field.setAccessible(true);
            map = (Map<TaskList, Pair<TaskListCtrl, Parent>>) field.get(sut);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    @Test
    void removeChild() {
        sut.removeChild(defaultChildEl[0]);

        Platform.runLater(() -> {
            assertFalse(container.getChildren().contains(defaultChildUI[0]));
            assertTrue(container.getChildren().contains(defaultChildUI[1]));
        });
    }

    @Test
    void addChild()  {
        TaskList newChild = new TaskList("C");
        newChild.setId(3L);
        sut.addOrUpdateChild(newChild);

        Platform.runLater(() -> {
            for (int i = 0; i < 2; i++) {
                assertTrue(container.getChildren().contains(defaultChildUI[i]));
            }
            assertEquals(3, container.getChildren().size());
            assertTrue(getUIMap().containsKey(newChild));
            assertEquals(1, numConsumerCalls);
        });
    }


    @Test
    void updateChild() {
        TaskList updatedChild = new TaskList("C");
        updatedChild.setId(defaultChildEl[0].getId());
        TaskListCtrl ret = sut.addOrUpdateChild(updatedChild);

        Platform.runLater(() -> {
            assertTrue(container.getChildren().contains(defaultChildUI[1]));
            assertEquals(2, container.getChildren().size());
            assertTrue(getUIMap().containsKey(updatedChild));
            assertEquals(ret, getUIMap().get(updatedChild).getKey());
            assertEquals(1, numConsumerCalls);
        });
    }

    @Test
    void clear() {
        sut.clear();

        Platform.runLater(() -> {
            assertEquals(0, container.getChildren().size());
            assertTrue(getUIMap().isEmpty());
        });
    }

    @Test
    void updateChildren() {
        List<TaskList> updated = new ArrayList<>();

        TaskList updatedChild = new TaskList("C");
        updatedChild.setId(defaultChildEl[0].getId());
        updated.add(updatedChild);

        TaskList newChild = new TaskList("C");
        newChild.setId(3L);
        updated.add(newChild);

        sut.updateChildren(updated);

        Platform.runLater(() -> {
            assertEquals(2, container.getChildren().size());
            assertEquals(2, numConsumerCalls);

            assertTrue(getUIMap().containsKey(updatedChild));
            assertTrue(getUIMap().containsKey(newChild));

            for (int i = 0; i < 2; i++) {
                assertFalse(
                        container.getChildren().contains(defaultChildUI[i])
                );
                assertFalse(getUIMap().containsKey(defaultChildEl[i]));
            }
        });
    }

    @Test
    void getChildrenCtrls() {
        var ret = sut.getChildrenCtrls();
        for (TaskListCtrl ctrl : defaultChildCtrl) {
            assertTrue(ret.contains(ctrl));
        }
    }

    @Test
    void setUpdatedChildConsumer()
            throws NoSuchFieldException, IllegalAccessException {
        Consumer<TaskListCtrl> newConsumer = taskListCtrl -> {};
        sut.setUpdatedChildConsumer(newConsumer);
        Field consumerField =
                ChildrenManager.class.getDeclaredField("updatedChildConsumer");
        consumerField.setAccessible(true);
        assertEquals(newConsumer, consumerField.get(sut));
    }
}