package commons;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.LinkedList;
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
        taskLists = new LinkedList<TaskList>();
    }

    /**
     * board constructor
     * @param code board code
     * @param title board title
     */
    public Board(String code, String title) {
        this.code = code;
        this.title = title;
        taskLists = new LinkedList<TaskList>();
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

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

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
