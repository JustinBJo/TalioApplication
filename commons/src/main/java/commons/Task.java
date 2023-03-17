package commons;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    private String description;
    @OneToMany(cascade =  CascadeType.PERSIST)
    private List<Subtask> subtasks;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Tag> tags;

    /**
     * empty constructor for object mapper
     */
    public Task() {

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
     * get the id of the task
     * @return the id of the task
     */
    public long getId() {
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
    public void setTitle(String title){
        this.title=title;
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
    public void setDescription(String description){
        this.description=description;
    }

    /**
     * get the subtasks of the task
     * @return the subtasks of the task
     */
    public List<Subtask> getSubtasks() {
        return subtasks;
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
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return getId() == task.getId();
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
