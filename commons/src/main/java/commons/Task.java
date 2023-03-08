package commons;

import javax.persistence.*;
import java.util.List;

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

    public Task(){}

    public Task(String title, String description, List<Subtask> subtasks, List<Tag> tags){
        this.title = title;
        this.description = description;
        this.subtasks = subtasks;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public List<Tag> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return id == task.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", subtasks=" + subtasks +
                ", tags=" + tags +
                '}';
    }
}
