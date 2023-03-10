package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * empty constructor
     */
    public TaskList() {

    }

    /**
     * get the id of the task list
     * @return the id of the task list
     */
    public Long getId() {
        return id;
    }

    /**
     * set the id of the task list
     * @param id the id of the task list
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * constructor
     * @param title the title of the task list
     */
    public TaskList(String title) {
        this.title = title;
        this.tasks = new ArrayList<Task>();
    }

    /**
     * get the title of the task list
     * @return the title of the task list
     */
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
     * @param tasks the tasks of the task list
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskList taskList = (TaskList) o;
        return Objects.equals(id, taskList.id)
                && Objects.equals(title, taskList.title)
                && Objects.equals(tasks, taskList.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, tasks);
    }
}
