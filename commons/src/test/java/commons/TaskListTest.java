package commons;

import org.junit.jupiter.api.Test;

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
    public void equalsTestNormal() {
        TaskList taskList = new TaskList("Test List");
        TaskList other1 = taskList;
        assertTrue(taskList.equals(other1));
    }

    @Test
    public void equalsTestForced() {
        TaskList tl1 = new TaskList("test one");
        tl1.setId(1L);
        TaskList tl2 = new TaskList("test two");
        tl2.setId(2L);

        assertFalse(tl1.equals(tl2));

        tl2.setId(tl1.getId());
        assertTrue(tl1.equals(tl2));
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
    public void hasToString() {
        String actual = new TaskList("Test List").toString();
        assertTrue(actual.contains(TaskList.class.getSimpleName()));
        assertTrue(actual.contains("Test List"));
    }
}
