package commons;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@Table
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Task> tasks;

    /**
     * empty constructor for object mapper
     */
    public TaskList() {

    }

    /**
     * Create a new task list.
     * @param title the title of the task list
     */
    public TaskList(String title) {
        this.title = title;
        this.tasks = new ArrayList<Task>();
    }

    public long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    /**
     * get the tasks of the task list
     * @return the tasks of the task list
     */
    public List<Task> getTasks() {
        return this.tasks;
    }

    /**
     * set the title of the task list
     * @param title the title of the task list
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * set the tasks of the task list
     * @param tasks  the tasks of the task list
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != TaskList.class) return false;

        TaskList other = (TaskList) obj;

        if (this.id == other.id) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, tasks);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

}
