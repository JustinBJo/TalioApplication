package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private Board b1;
    private Board b2;

    @BeforeEach
    void setup() {
        b1 = new Board("b1");
        b2 = new Board("b2");

        List<Board> boards = new ArrayList<>();

        boards.add(b1);
        boards.add(b2);

        user = new User("127.0.0.1", boards);
    }

    @Test
    void fullConstructorTest() {
        List<Board> expected = new ArrayList<>();
        expected.add(b1);
        expected.add(b2);

        assertEquals(user.getBoards(), expected);
        assertEquals(user.getIp(), "127.0.0.1");
    }

    @Test
    void ipConstructorTest() {
        User u = new User("127.0.0.1");
        assertEquals(u.getIp(), "127.0.0.1");
    }

    @Test
    void emptyConstructorTest() {
        User u = new User();
        assertEquals(u.getBoards().size(), 0);
    }

    @Test
    void testGetIp() {
        assertEquals(user.getIp(), "127.0.0.1");
    }

    @Test
    void testSetIp() {
        user.setIp("localhost");
        assertEquals(user.getIp(), "localhost");
    }

    @Test
    void testEquals() {
        List<Board> exp = new ArrayList<>();
        exp.add(b1);
        exp.add(b2);

        User user2 = new User("127.0.0.1", exp);

        assertTrue(user.equals(user2));
    }

    @Test
    void hashTestNotEqual() {
        List<Board> exp = new ArrayList<>();
        exp.add(new Board("b1"));
        exp.add(new Board("b2"));

        User user2 = new User("127.0.0.1", exp);

        assertNotEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void hashTestEqual() {
        assertEquals(user.hashCode(), user.hashCode());
    }

    @Test
    void testAddBoard() {
        Board b3 = new Board("b3");
        user.addBoard(b3);
        assertTrue(user.getBoards().contains(b3));
    }

    @Test
    void removeBoardTest() {
        Board b2 = user.getBoards().get(1);
        user.removeBoard(b2);
        assertEquals(user.getBoards().size(), 1);
    }

    @Test
    void getBoardsTest() {
        List<Board> exp = new ArrayList<>();
        exp.add(b1);
        exp.add(b2);

        assertEquals(user.getBoards(), exp);
    }

}
