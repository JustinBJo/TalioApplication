package server.api;

import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import server.database.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController sut;
    private UserRepository repo;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setup() {
        repo = new TestUserRepository();
        sut = new UserController(repo);
        request = Mockito.mock(HttpServletRequest.class);
    }

    @Test
    void findAllTest() {
        User u1 = new User();
        User u2 = new User();

        repo.save(u1);
        repo.save(u2);

        List<User> users = sut.findAll();

        assertTrue(users.contains(u1));
        assertTrue(users.contains(u2));
    }

    @Test
    void addUserTest() {
        User u = new User("127.0.0.1");
        sut.addUser(u);

        assertEquals(repo.getById("127.0.0.1"), u);
    }

    @Test
    void updateTest() {
        List<Board> boards = new ArrayList<>();

        Board b1 = new Board("b1");
        Board b2 = new Board("b2");

        boards.add(b1);
        boards.add(b2);

        User u = new User("127.0.0.1", boards);

        var res = sut.update(u);

        assertEquals(repo.getById("127.0.0.1"), u);
        assertEquals(res, u);
    }

    @Test
    void testMessageUpdate() {
        List<Board> boards = new ArrayList<>();

        Board b1 = new Board("b1");
        Board b2 = new Board("b2");

        boards.add(b1);
        boards.add(b2);

        User u = new User("127.0.0.1", boards);

        var res = sut.messageUpdate(u, u.getId().toString());

        assertEquals(repo.getById("127.0.0.1"), u);
        assertEquals(res, u);
    }

    @Test
    void getCurrentIpTest() {
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        assertEquals(sut.getCurrentIp(request), "127.0.0.1");
    }
}
