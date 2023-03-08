package commons;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ThemeTest {

    @Test
    void testConstructor() {
        var t = new Theme(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
        assertNotNull(t);
        assertEquals(Color.BLACK, t.getBoardColor());
        assertEquals(Color.BLACK, t.getListColor());
        assertEquals(Color.BLACK, t.getTaskColor());
        assertEquals(Color.BLACK, t.getSubtaskColor());
    }

    @Test
    void testEquals() {
        var a = new Theme(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
        var b = new Theme(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
        var c = new Theme(Color.WHITE, Color.BLACK, Color.BLACK, Color.BLACK);
        assertEquals(a, b);
        assertNotEquals(a, c);
        assertEquals(a,a);
    }

    @Test
    void testHashCode() {
        var a = new Theme(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
        var b = new Theme(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
        var c = new Theme(Color.WHITE, Color.BLACK, Color.BLACK, Color.BLACK);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(a.hashCode(), a.hashCode());
        assertNotEquals(a.hashCode(), c.hashCode());
    }
}