package commons;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String code;
    private String title;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<TaskList> taskLists;

    /**
     * board constructor
     */
    @SuppressWarnings("unused")
    public Board() {
        // for object mappers
        taskLists = new ArrayList<>();
    }

    /**
     * board constructor with no task lists and auto-generated code
     * @param title board title
     */
    public Board(String title) {
        this.title = title;
        this.code = generateCode();
        taskLists = new ArrayList<>();
    }

    /**
     * board constructor with no task lists
     * @param code board code
     * @param title board title
     */
    public Board(String code, String title) {
        this.code = code;
        this.title = title;
        taskLists = new ArrayList<>();
    }

    /**
     * board constructor
     * @param code board code
     * @param title board title
     * @param taskLists board task lists
     */
    public Board(String code, String title, List<TaskList> taskLists) {
        this.code = code;
        this.title = title;
        this.taskLists = taskLists;
    }

    /**
     * Generates a code for this board
     * @return generated code
     */
    private String generateCode() {
        // Hashes title and id then truncates a base-16 representation
        // of that hash to 6 characters
        String hash = Integer.toString(Objects.hash(title, id), 16);
        return (hash.length() < 6) ? hash : hash.substring(0, 6);
    }

    /**
     * get board id
     * @return board id
     */
    public long getId() {
        return id;
    }

    /**
     * set board id
     * @param id board id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * get board id
     * @return board id
     */
    public String getCode() {
        return code;
    }

    /**
     * set board code
     * @param code board code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * get board title
     * @return board title
     */
    public String getTitle() {
        return title;
    }

    /**
     * set board title
     * @param title board title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * get board task lists
     * @return board task lists
     */
    public List<TaskList> getTaskLists() {
        return taskLists;
    }

    /**
     * set board task lists
     * @param taskLists board task lists
     */
    public void setTaskLists(List<TaskList> taskLists) {
        this.taskLists = taskLists;
    }

    public boolean addTaskList(TaskList list) {
        if (this.taskLists == null) { this.taskLists = new ArrayList<>(); }
        return this.taskLists.add(list);
    }

    public boolean removeTaskList(TaskList list) {
        if (this.taskLists == null) { return false; }
        return this.taskLists.remove(list);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return id == board.id && Objects.equals(code, board.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
