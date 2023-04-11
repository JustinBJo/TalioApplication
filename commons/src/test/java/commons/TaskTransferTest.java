package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTransferTest {

    @Test
    public void emptyConstructor() {
        TaskTransfer t = new TaskTransfer();
        assertEquals(-1, t.newParentId);
        assertEquals(new Task(), t.newTask);
        assertEquals(-1, t.oldParentId);
        assertEquals(new Task(), t.oldTask);
    }

    @Test
    public void constructor() {
        Long a = 1L;
        Long b = 2L;
        Task t1 = new Task("t1");
        Task t2 = new Task("t2");

        TaskTransfer t = new TaskTransfer(
            a, t1, b, t2
        );

        assertEquals(a, t.oldParentId);
        assertEquals(t1, t.oldTask);
        assertEquals(b, t.newParentId);
        assertEquals(t2, t.newTask);
    }
}