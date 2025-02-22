package commons;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Task implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    private String description;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Subtask> subtasks;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Tag> tags;

    /**
     * empty constructor for object mapper
     */
    public Task() {
        this.subtasks = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    /**
     * Create a new task.
     * @param title the title of the task
     * @param description the description of the task
     * @param subtasks the subtasks of the task
     * @param tags the tags of the task
     */
    public Task(String title, String description,
                List<Subtask> subtasks, List<Tag> tags) {
        this.title = title;
        this.description = description;
        this.subtasks = subtasks;
        this.tags = tags;
    }

    /**
     * Creates a new task given only a title
     * @param title task title
     */
    public Task(String title) {
        this.title = title;
        this.description = "";
        this.subtasks = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    /**
     * Creates a new task given only a title and description
     * @param title task title
     * @param description task description
     */
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.subtasks = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    /**
     * get the id of the task
     * @return the id of the task
     */
    public Long getId() {
        return id;
    }

    /**
     * set the id of the task
     * @param id the id of the task
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * get the title of the task
     * @return the title of the task
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the task
     * @param title new title of task
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * set the title of the task
     * @return the title of the task
     */

    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the task
     * @param description new description of task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get the subtasks of the task
     * @return the subtasks of the task
     */
    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    /**
     * Remove a subtask from this task
     * @param subtask the subtask that is being removed
     * @return true if the subtask has been removed successfully,
     * false otherwise
     */
    public boolean removeSubtask(Subtask subtask) {
        return this.subtasks.remove(subtask);
    }

    /**
     * Adds a subtask to the task
     * @param subtask the new subtask to be added
     * @return true if subtask was added successfully, false otherwise
     */
    public boolean addSubtask(Subtask subtask) {
        if (this.subtasks == null) this.subtasks = new ArrayList<>();
        return this.subtasks.add(subtask);
    }

    /**
     * set the subtasks of the task
     * @param subtasks the subtasks of the task
     */
    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    /**
     * get the tags of the task
     * @return the tags of the task
     */
    public List<Tag> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task other = (Task) o;
        return (getId() == null && other.getId() == null
                ||  getId().equals(other.getId()))
                && (getTitle() == null && other.getTitle() == null
                || getTitle().equals(other.getTitle()));
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
