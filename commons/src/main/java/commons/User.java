package commons;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Entity
public class User implements IEntity {
    @Id
    private String ip;

    private Long id;

    @OneToMany(cascade =  CascadeType.PERSIST)
    private List<Board> joinedBoards;

    /**
     * Constructor with no parameters
     */
    public User() {
        this.joinedBoards = new ArrayList<>();
    }

    /**
     * Constructor with ip parameter
     * @param ip the ip of the user
     */
    public User(String ip) {
        this.joinedBoards = new ArrayList<>();
        setId(ip);
        this.ip = ip;
    }

    /**
     * Constructor with ip and joinedBoards
     * @param ip ip parameter
     * @param joinedBoards the user's joined boards
     */
    public User(String ip, List<Board> joinedBoards) {
        setId(ip);
        this.ip = ip;
        this.joinedBoards = joinedBoards;
    }

    /**
     * getter for the ip
     * @return user's ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * setter for the ip
     * @param ip the ip to be set
     */
    public void setIp(String ip) {
        setId(ip);
        this.ip = ip;
    }

    /**
     * determines if two users are equal
     * @param o the other user
     * @return true if equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(ip, user.ip) &&
                Objects.equals(joinedBoards, user.joinedBoards);
    }

    /**
     * hash function for the user
     * @return the hashcode of the user
     */
    @Override
    public int hashCode() {
        return Objects.hash(ip, joinedBoards);
    }

    /**
     * adds a board to the user's collection
     * @param b the board to be added
     */
    public void addBoard(Board b) {
        joinedBoards.add(b);
    }

    /**
     * removes a board from the user's collection
     *
     * @param b the board to be removed
     * @return success state of removal
     */
    public boolean removeBoard(Board b) {
        return joinedBoards.remove(b);
    }

    /**
     * returns the user's boards
     * @return the list of boards
     */
    public List<Board> getBoards() {
        return joinedBoards;
    }

    @Override
    public Long getId() {
        return id;
    }

    private void setId(String ip) {
        if (ip != null) {
            this.id = Long.parseLong(ip.replaceAll("[^0-9]", ""));
        }
    }
}
