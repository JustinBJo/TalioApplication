package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    @Test
    void tagConstructor(){
        var tag=new Tag("Requirement", "BLUE");
        assertNotNull(tag);
        assertEquals("Requirement", tag.getName());
        assertEquals("BLUE", tag.getColor());
    }

    @Test
    void testSetName(){
        var t=new Tag("R1", "RED");
        assertEquals("R1", t.getName());
        t.setName("R2");
        assertEquals("R2", t.getName());
    }

    @Test
    void testSetColor(){
        var t=new Tag("R1", "RED");
        assertEquals("RED", t.getColor());
        t.setColor("BLUE");
        assertEquals("BLUE", t.getColor());
    }


    @Test
    void testEquals() {
        var tag1 = new Tag("R1", "RED");
        var tag2 = new Tag("R1", "RED");
        assertEquals(tag1,tag2);
        assertEquals(tag1,tag1);
    }

    @Test
    void testNotEquals(){
        var tag1 = new Tag("R1", "RED");
        var tag2 = new Tag("R2", "BLUE");
        assertNotEquals(tag1,tag2);
    }

    @Test
    void testEqualHashCode() {
        var tag1 = new Tag("R1", "RED");
        var tag2 = new Tag("R1", "RED");
        assertEquals(tag1.hashCode(),tag2.hashCode());
    }

    @Test
    void testNotEqualHashCode() {
        var tag1 = new Tag("R1", "RED");
        var tag2 = new Tag("R2", "BLUE");
        assertNotEquals(tag1.hashCode(),tag2.hashCode());
    }

    @Test
    void testToString() {
        var tag1 = new Tag("R1", "RED");
        String toString = "Tag{id=0, name='R1', color=RED}";
        assertEquals(toString, tag1.toString());
    }
}