package commons;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Board implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String code;
    private String title;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
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
        String uniqueCode = UUID.randomUUID().toString().substring(0, 3);

        int hour = LocalDateTime.now().getHour();
        int minute = LocalDateTime.now().getMinute();
        String timeCode = Integer.toString(hour + minute);

        String randCode = Integer.toString(new Random().nextInt(10));

        return uniqueCode + timeCode + randCode;
    }

    /**
     * get board id
     * @return board id
     */
    public Long getId() {
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

    /**
     * Add task list to list of task lists
     * @param list the task list to be added
     * @return true iff added successfully
     */
    public boolean addTaskList(TaskList list) {
        if (this.taskLists == null) this.taskLists = new ArrayList<>();
        return this.taskLists.add(list);
    }

    /**
     * Remove a task list from the list of task lists
     * @param list the task list to be removed
     * @return true iff removed successfully
     */
    public boolean removeTaskList(TaskList list) {
        if (this.taskLists == null) return false;
        return this.taskLists.remove(list);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return getId().equals(board.getId())
                && Objects.equals(code, board.code);
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
