package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskTest {

    @Test
    public void checkConstructor() {
        var t = new Task("Task Title", "Description", new ArrayList<>(), new ArrayList<>());
        assertEquals("Task Title", t.getTitle());
        assertEquals("Description", t.getDescription());
        assertEquals(0, t.getSubtasks().size());
        assertEquals(0, t.getTags().size());
    }

    @Test
    public void equalsHashCode() {
        var a = new Task("Task Title", "Description", new ArrayList<>(), new ArrayList<>());
        var b = new Task("Task Title", "Description", new ArrayList<>(), new ArrayList<>());
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var a = new Task("Task Title1", "Description", new ArrayList<>(), new ArrayList<>());
        var b = new Task("Task Title2", "Description", new ArrayList<>(), new ArrayList<>());
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        var actual = new Task("Task Title", "Description", new ArrayList<>(), new ArrayList<>()).toString();
        assertTrue(actual.contains(Task.class.getSimpleName()));
        assertTrue(actual.contains("Title"));
        assertTrue(actual.contains("Description"));
    }
}
