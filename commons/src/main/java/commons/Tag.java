package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String color;
    //TODO: MAKE IT COLOR TYPE

    @SuppressWarnings("unused")
    private Tag() {
        // for object mapper
    }

    /**
     * Create a new tag.
     * @param name the name of the tag
     * @param color the color of the tag
     */
    public Tag(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * get the name of the tag
     * @return the name of the tag
     */
    public String getName() {
        return name;
    }

    /**
     * set the name of the tag
     * @param name the name of the tag
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get the color of the tag
     * @return the color of the tag
     */
    public String getColor() {
        return color;
    }

    /**
     * set the color of the tag
     * @param color the color of the tag
     */
    public void setColor(String color) {
        this.color = color;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name) && Objects.equals(color, tag.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color=" + color +
                '}';
    }
}
