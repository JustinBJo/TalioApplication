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

    public TaskList() {

    }

    public TaskList(String title) {
        this.title = title;
        this.tasks = new ArrayList<Task>();
    }

    public String getTitle() {
        return this.title;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskList taskList = (TaskList) o;

        if (!Objects.equals(id, taskList.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
