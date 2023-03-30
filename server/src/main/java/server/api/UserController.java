package server.api;

import javax.servlet.http.HttpServletRequest;

import commons.Board;
import commons.User;
import org.springframework.web.bind.annotation.*;
import server.database.UserRepository;


import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository repo;

    /**
     * constructor for the UserController
     * @param repo the repository used
     */
    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * gets a list of all users from the database
     * @return the list of users
     */
    @GetMapping( path = {"", "/"} )
    public List<User> findAll() {
        return repo.findAll();
    }

    /**
     * returns the current ip of the user
     * @param request the request processed
     * @return the ip of the client configuration
     */
    @GetMapping("/ip")
    public String getCurrentIp(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        return ipAddress;
    }

    /**
     * adds a user to the repository
     * @param user the user to be added
     */
    @PostMapping("/add")
    public void addUser(@RequestBody User user) {
        repo.save(user);
    }

    /**
     * updates the user's state
     * @param user the user to be updated
     * @return the current list of joined boards
     */
    @PutMapping("/save")
    public List<Board> update(@RequestBody User user) {
        repo.save(user);
        return user.getBoards();
    }
}