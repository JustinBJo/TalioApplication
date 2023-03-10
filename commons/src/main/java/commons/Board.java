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

    @SuppressWarnings("unused")
    public Board() {
        // for object mappers
        taskLists = new LinkedList<TaskList>();
    }

    public Board(String code, String title) {
        this.code = code;
        this.title = title;
        taskLists = new LinkedList<TaskList>();
    }

    public Board(String code, String title, List<TaskList> taskLists) {
        this.code = code;
        this.title = title;
        this.taskLists = taskLists;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TaskList> getTaskLists() {
        return taskLists;
    }

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
