package commons;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Subtask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    private boolean completed;

    /**
     * empty constructor
     */
    public Subtask() {

    }

    /**
     * Create a new subtask.
     * @param title the title of the subtask
     * @param completed whether the subtask is completed
     */
    public Subtask(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    /**
     * get the title of the subtask
     * @return the title of the subtask
     */
    public String getTitle() {
        return title;
    }

    /**
     * set the title of the subtask
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * if the subtask is completed
     * @return true iff the subtask is completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Setter for the complete attribute
     * @param completed the new value for "completed"
     */
    public void setCompleted(boolean completed){
        this.completed=completed;
    }

    /**
     * Decides whether the given object is equal to this subtask
     * @param o object to be compared to
     * @return whether the objects are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return id == subtask.id && completed == subtask.completed &&
                Objects.equals(title, subtask.title);
    }

    /**
     * generates hashCode for the subtask
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, completed);
    }

    /**
     * Sets the id of the subtask
     * @param id the id to be set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * returns the id of the subtask
     * @return the id
     */
    public long getId() {
        return id;
    }
}
