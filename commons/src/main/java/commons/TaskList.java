package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class TaskList implements IEntity {

    @Id
    @GeneratedValue()
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

    /**
     * Add a new task to this tasklist
     * @param task the task that is being added to this list
     * @return true if the task has been added successfully, false otherwise
     */
    public boolean addTask(Task task) {
        if (this.tasks == null) {
            this.tasks = new ArrayList<>();
        }
        return this.tasks.add(task);
    }

    /**
     * Remove a task from this tasklist
     * @param task the task that is being removed
     * @return true if the task has been removed successfully, false otherwise
     */
    public boolean removeTask(Task task) {
        return this.tasks.remove(task);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof TaskList)) return false;

        TaskList taskList = (TaskList) o;
        return (getId() == null && taskList.getId() == null
                ||  getId().equals(taskList.getId()))
                && (getTitle() == null && taskList.getTitle() == null
                || getTitle().equals(taskList.getTitle()))
                && (getTasks() == null && taskList.getTasks() == null
                || getTasks().equals(taskList.getTasks()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, tasks);
    }

    @Override
    public String toString() {
        return "TaskList{" + title +
                ", " + tasks +
                '}';
    }
}
