package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    private List<TaskList> emptyListOfTaskList() {
        return new ArrayList<>();
    }

    private List<TaskList> filledListOfTaskList() {
        List<TaskList> taskListList = new ArrayList<>();
        taskListList.add(new TaskList("a"));
        taskListList.add(new TaskList("b"));
        return taskListList;
    }

    @Test
    void checkConstructor() {
        Board emptyBoard = new Board();
        assertNull(emptyBoard.getCode());
        assertNull(emptyBoard.getTitle());
        assertEquals(emptyListOfTaskList(), emptyBoard.getTaskLists());

        Board titleOnlyBoard = new Board("MyBoard");
        assertNotNull(titleOnlyBoard.getCode());
        assertEquals("MyBoard", titleOnlyBoard.getTitle());
        assertEquals(emptyListOfTaskList(), emptyBoard.getTaskLists());

        Board noTaskListBoard = new Board("123", "MyBoard");
        assertEquals("123", noTaskListBoard.getCode());
        assertEquals("MyBoard", noTaskListBoard.getTitle());
        assertEquals(emptyListOfTaskList(), noTaskListBoard.getTaskLists());

        Board fullBoard = new Board("123", "MyBoard", filledListOfTaskList());
        assertEquals("123", fullBoard.getCode());
        assertEquals("MyBoard", fullBoard.getTitle());
        assertEquals(filledListOfTaskList(), fullBoard.getTaskLists());
    }

    @Test
    void hasToString() {
        String actual = new Board("54321", "joe").toString();
        assertTrue(actual.contains(Board.class.getSimpleName()));
        assertTrue(actual.contains("code"));
        assertTrue(actual.contains("54321"));
        assertTrue(actual.contains("title"));
        assertTrue(actual.contains("joe"));
    }

    @Test
    void testEquals() {
        Board a = new Board("a", "a", emptyListOfTaskList());
        Board b = new Board("a", "b");
        Board c = new Board("c", "b", emptyListOfTaskList());
        assertEquals(a, b);
        assertNotEquals(a, c);
        assertNotEquals(b, c);
    }

    @Test
    void equalsHashCode() {
        Board a = new Board("123", "Test Board");
        Board b = new Board("123", "Test Board");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void notEqualsHashCode() {
        Board a = new Board("123", "Test Board");
        Board b = new Board("456", "Test Board");
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void addTaskList() {
        Board a = new Board("Test Board");
        assertTrue(a.getTaskLists().isEmpty());

        TaskList tl = new TaskList("Test List");
        assertTrue(a.addTaskList(tl));
        assertTrue(a.getTaskLists().contains(tl));
    }

    @Test
    void addTaskListWhenTaskListsIsNull() {
        Board a = new Board("Test Board");
        a.setTaskLists(null);
        assertNull(a.getTaskLists());

        TaskList tl = new TaskList("Test List");
        assertTrue(a.addTaskList(tl));
        assertTrue(a.getTaskLists().contains(tl));
    }

    @Test
    void removeTaskList() {
        Board a = new Board("Test Board");
        assertTrue(a.getTaskLists().isEmpty());

        TaskList tl = new TaskList("Test List");
        assertTrue(a.addTaskList(tl));
        assertTrue(a.getTaskLists().contains(tl));

        assertTrue(a.removeTaskList(tl));
        assertFalse(a.getTaskLists().contains(tl));
    }

    @Test
    void failRemoveTaskList() {
        Board a = new Board("Test Board");
        assertTrue(a.getTaskLists().isEmpty());

        TaskList tl = new TaskList("Test List");
        assertFalse(a.getTaskLists().contains(tl));
        assertFalse(a.removeTaskList(tl));
    }

    @Test
    void testGettersAndSetters() {
        String defaultCode = "123";
        String defaultTitle = "Test Board";
        List<TaskList> defaultListOfTaskList = emptyListOfTaskList();
        long defaultId = 0;

        Board board = new Board(
                defaultCode,
                defaultTitle,
                defaultListOfTaskList
        );
        board.setId(defaultId);

        // Getters
        assertEquals(defaultCode, board.getCode());
        assertEquals(defaultTitle, board.getTitle());
        assertEquals(defaultListOfTaskList, board.getTaskLists());
        assertEquals(defaultId, board.getId());

        String differentCode = "456";
        String differentTitle = "Changed Board";
        List<TaskList> differentListOfTaskList = null;
        long differentId = 10;

        // Setters
        board.setId(differentId);
        assertEquals(differentId, board.getId());
        board.setCode(differentCode);
        assertEquals(differentCode, board.getCode());
        board.setTitle(differentTitle);
        assertEquals(differentTitle, board.getTitle());
        board.setTaskLists(differentListOfTaskList);
        assertEquals(differentListOfTaskList, board.getTaskLists());
    }

    @Test
    void testGenerateCode() {
        // Test whether two instances of Boards have the same code
        Board a = new Board("a");
        Board b = new Board("b");

        assertNotEquals(a.getCode(), b.getCode());
    }

    @Test
    void testGenerateCodeExtended() {
        // Test whether the generateCode method creates a duplicate code
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            Board b = new Board(Integer.toString(i));
            list.add(b.getCode());
        }

        String checker1 = new Board("checker").getCode();
        String checker2 = new Board("0").getCode();

        assertFalse(list.contains(checker1));
        assertFalse(list.contains(checker2));
    }
}
