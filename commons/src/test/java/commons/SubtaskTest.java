package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    protected Subtask sut;

    @BeforeEach
    void set() {
         sut = new Subtask("test", true);
    }

    @Test
    void checkEmptyConstructor() {
        Subtask empty = new Subtask();
        assertNull(empty.getTitle());
    }

    @Test
    void testConstructor() {
        assertEquals(sut.getTitle(), "test");
        assertEquals(sut.isCompleted(), true);
    }

    @Test
    void getTitle() {
        assertEquals(sut.getTitle(), "test");
    }

    @Test
    void setTitle() {
        sut.setTitle("newTitle");
        assertEquals("newTitle", sut.getTitle());
    }

    @Test
    void isCompleted() {
        assertEquals(sut.isCompleted(), true);
    }

    @Test
    void testEquals() {
        Subtask k = new Subtask("test", true);
        assertEquals(k, sut);
    }

    @Test
    void testHashCode() {
        Subtask k = new Subtask("test", true);
        assertEquals(k, sut);
        assertEquals(sut.hashCode(), k.hashCode());
    }

    @Test
    void testSetId() {
        sut.setId(1);
        assertEquals(1, sut.getId());
    }

    @Test
    void testGetId() {
        sut.setId(1);
        assertEquals(1, sut.getId());
    }
}