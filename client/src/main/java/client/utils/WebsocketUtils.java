package client.utils;

import commons.Board;
import commons.Subtask;
import commons.Task;
import commons.TaskList;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import javafx.util.Pair;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class WebsocketUtils {
    private StompSession session;

    public void updateServer(String address) {
        if (session != null) {
            session.disconnect();
        }
        session = connect("ws://" + address + "websocket");
    }

    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * Registers to receive websocket messages from destination
     * @param dest websocket endpoint that this subscribes to
     * @param type payload type returned to the consumer
     * @param consumer what happens with the received message
     * @param <T> payload type returned to the consumer
     */
    public <T> StompSession.Subscription registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
        return session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                if (payload == null) {
                    return;
                }
                consumer.accept((T) payload);
            }
        });
    }

    public void addTaskList(TaskList taskList, Board board) {
        session.send("/app/taskList/add/" + board.getId(), taskList);
    }

    public void deleteTaskList(TaskList taskList) {
        session.send("/app/taskList/delete/" + taskList.getId(), taskList);
    }

    public void updateTaskList(TaskList taskList, String title) {
        session.send("/app/taskList/update/" + taskList.getId() + "/" + title, taskList);
    }

    /**
     * Update the title of the given board using the board/update endpoint
     *
     * @param board   the board that is being updated
     * @param newName the new name of the board
     */
    public void updateBoard(Board board, String newName) {
        session.send("/app/board/update/" + board.getId() + "/" + newName, board);
    }

    /**
     * Update the title of the given task using the tasks/updateTitle endpoint
     *
     * @param task   the task that is being updated
     * @param newTitle the new title of the task
     */
    public void updateTaskTitle(Task task, String newTitle) {
        session.send("/app/task/updateTitle/" + task.getId() + "/" + newTitle, task);
    }

    /**
     * Update the description of the given task,
     * using the tasks/updateDescription endpoint
     *
     * @param task   the task that is being updated
     * @param newDescription the new description of the task
     */
    public void updateTaskDescription(Task task, String newDescription) {
        session.send("/app/task/updateDescription/" + task.getId() + "/" + newDescription, task);
    }

    /**
     * Deletes a task from the server
     * @param task the task to be deleted
     */
    public void deleteTask(Task task) {
        session.send("/app/task/delete/" + task.getId(), task);
    }

    /**
     * Method used to insert a task into the database
     *
     * @param task the task to be added to the database
     * @param parentTaskList task list that will hold this task
     */
    public void addTask(Task task, TaskList parentTaskList) {
        session.send("/app/task/add/" + parentTaskList.getId(), task);
    }

    /**
     * Method used to insert a subtask into the database
     *
     * @param subtask the subtask to be added to the database
     * @param parentTask task that will hold this subtask
     */
    public void addSubtask(Subtask subtask, Task parentTask) {
        session.send("/app/subtask/add/" + parentTask.getId(), subtask);
    }

    /**
     * Deletes a subtask from the server
     * @param subtask the subtask to be deleted
     */
    public void deleteSubtask(Subtask subtask) {
        session.send("/app/subtask/delete/" + subtask.getId(), subtask);
    }

    /**
     * Updates the title of the subtask in the database
     * @param subtask the subtask to be edited
     * @param newTitle the new title of the subtask
     */
    public void updateSubtask(Subtask subtask, String newTitle) {
        session.send("/app/subtask/update/" + subtask.getId() + "/" + newTitle, subtask);
    }
}
