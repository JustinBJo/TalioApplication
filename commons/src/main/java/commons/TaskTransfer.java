package commons;

public class TaskTransfer {
    public Long oldParentId;
    public Task oldTask;
    public Long newParentId;
    public Task newTask;


    /**
     * Empty constructor for object mapper
     */
    public TaskTransfer() {
        oldParentId = -1L;
        oldTask = new Task();
        newParentId = -1L;
        newTask = new Task();
    }

    public TaskTransfer(Long oldParentId, Task oldTask, Long newParentId, Task newTask) {
        this.oldParentId = oldParentId;
        this.oldTask = oldTask;
        this.newParentId = newParentId;
        this.newTask = newTask;
    }
}