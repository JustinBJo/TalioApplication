package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskListTest {
    @Test
    public void checkConstructor() {
        TaskList taskList = new TaskList("Test List");
        assertEquals(taskList.getTitle(), "Test List");
        assertTrue(taskList.getTasks().isEmpty());
    }

    @Test
    void checkEmptyConstructor() {
        TaskList taskList = new TaskList();
        assertEquals(null, taskList.getTitle());
        assertEquals(null, taskList.getTasks());
    }

    @Test
    void checkSetId() {
        TaskList taskList = new TaskList("t");
        taskList.setId(100L);
        assertEquals(100L, taskList.getId());
    }

    @Test
    public void equalsTestNormal() {
        TaskList taskList = new TaskList("Test List");
        TaskList other1 = taskList;
        assertTrue(taskList.equals(other1));
    }

    @Test
    public void equalsHashCode() {
        TaskList a = new TaskList("Test List");
        TaskList b = new TaskList("Test List");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        TaskList a = new TaskList("Test List");
        TaskList b = new TaskList("Test List2");
        assertNotEquals(a.hashCode(), b.hashCode());
    }


    @Test
    public void titleTest() {
        TaskList a = new TaskList("Test List");
        a.setTitle("Title");
        assertEquals(a.getTitle(), "Title");
    }

    @Test
    public void tasksTest() {
        TaskList a = new TaskList("Test List");
        List<Task> tasks = new ArrayList<>();
        Task t1 = new Task("task1", "example task 1", new ArrayList<>(),
                new ArrayList<>());
        tasks.add(t1);
        tasks.add(new Task("task2", "example task 2", new ArrayList<>(),
                new ArrayList<>()));
        a.setTasks(tasks);
        assertEquals(a.getTasks(), tasks);
        assertTrue(a.getTasks().size() == 2);
        a.removeTask(t1);
        assertTrue(a.getTasks().size() == 1);
    }

    @Test
    public void addTaskTest() {
        TaskList a = new TaskList("Test List");
        Task one = new Task("task1", "example task 1", new ArrayList<>(),
                new ArrayList<>());
        Task two = new Task("task2", "example task 2", new ArrayList<>(),
                new ArrayList<>());

        a.addTask(one);
        a.addTask(two);

        List<Task> tasks = new ArrayList<>();
        tasks.add(one);
        tasks.add(two);

        assertEquals(a.getTasks(), tasks);
        assertTrue(a.getTasks().size() == 2);
    }
}
