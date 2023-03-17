package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskTest {

    @Test
    public void checkConstructor() {
        var t = new Task("Task Title", "Description",
                new ArrayList<>(), new ArrayList<>());
        assertEquals("Task Title", t.getTitle());
        assertEquals("Description", t.getDescription());
        assertEquals(0, t.getSubtasks().size());
        assertEquals(0, t.getTags().size());
    }

    @Test
    void checkEmptyConstructor() {
        Task emptyTask = new Task();
        assertNull(emptyTask.getTitle());
        assertNull(emptyTask.getDescription());
    }

    @Test
    public void testToString() {
        var actual = new Task("Task Title", "Description",
                new ArrayList<>(), new ArrayList<>()).toString();
        assertTrue(actual.contains(Task.class.getSimpleName()));
        assertTrue(actual.contains("Title"));
        assertTrue(actual.contains("Description"));
    }

    @Test
    void getId() {
        var a = new Task("Task Title1", "Description",
                new ArrayList<>(), new ArrayList<>());
        assertEquals(0, a.getId());
    }

    @Test
    void setId() {
        var a = new Task("Task Title1", "Description",
                new ArrayList<>(), new ArrayList<>());
        a.setId(10);
        assertEquals(10, a.getId());
    }

    @Test
    void getTitle() {
        var a = new Task("Task Title1", "Description",
                new ArrayList<>(), new ArrayList<>());
        assertEquals("Task Title1", a.getTitle());
    }

    @Test
    void setTitle() {
        var a = new Task("title", "description",
                new ArrayList<>(), new ArrayList<>());
        a.setTitle("new title");
        assertEquals("new title", a.getTitle());
    }

    @Test
    void getDescription() {
        var a = new Task("Task Title1", "Description",
                new ArrayList<>(), new ArrayList<>());
        assertEquals("Description", a.getDescription());
    }

    @Test
    void setDescription() {
        var a = new Task("title", "description",
                new ArrayList<>(), new ArrayList<>());
        a.setDescription("new description");
        assertEquals("new description", a.getDescription());
    }

    @Test
    void getSubtasks() {
        var a = new Task("Task Title1", "Description",
                new ArrayList<>(), new ArrayList<>());
        List<Subtask> comp = new ArrayList<>();
        assertEquals(comp, a.getSubtasks());
    }

    @Test
    void getTags() {
        var a = new Task("Task Title1", "Description",
                new ArrayList<>(), new ArrayList<>());
        List<Tag> comp = new ArrayList<>();
        assertEquals(comp, a.getTags());
    }

    @Test
    public void equalsTest1() {
        var a = new Task("Task Title1", "Description",
                new ArrayList<>(), new ArrayList<>());
        var b = a;
        assertEquals(a, b);
    }

    @Test
    public void equalsTest2() {
        var a = new Task("Task Title1", "Description",
                new ArrayList<>(), new ArrayList<>());
        a.setId(1L);
        var b = new Task("Task Title2", "Description",
                new ArrayList<>(), new ArrayList<>());
        b.setId(2L);
        assertNotEquals(a, b);
        b.setId(a.getId());
        assertEquals(a, b);
    }

    @Test
    public void equalsHashCode() {
        var a = new Task("Task Title", "Description",
                new ArrayList<>(), new ArrayList<>());
        var b = new Task("Task Title", "Description",
                new ArrayList<>(), new ArrayList<>());
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var a = new Task("Task Title1", "Description",
                new ArrayList<>(), new ArrayList<>());
        a.setId(1L);
        var b = new Task("Task Title2", "Description",
                new ArrayList<>(), new ArrayList<>());
        b.setId(2L);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
        b.setId(a.getId());
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        var actual = new Task("Task Title", "Description",
                new ArrayList<>(), new ArrayList<>()).toString();
        assertTrue(actual.contains(Task.class.getSimpleName()));
        assertTrue(actual.contains("Title"));
        assertTrue(actual.contains("Description"));
    }

}
