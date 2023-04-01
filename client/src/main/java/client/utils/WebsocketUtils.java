package client.utils;

import commons.Board;
import commons.TaskList;
import javafx.util.Pair;
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

public class WebsocketUtils {
    private StompSession session;
    private final Map<Pair<Object, String>, StompSession.Subscription> subscriptionMap = new HashMap<>();

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
     * @param subscriber object linked to registration, forced to
     *                   have only a single subscription in each destination
     * @param dest websocket endpoint that this subscribes to
     * @param type payload type returned to the consumer
     * @param consumer what happens with the received message
     * @param <T> payload type returned to the consumer
     */
    public <T> StompSession.Subscription registerForMessages(Object subscriber, String dest, Class<T> type, Consumer<T> consumer) {
        if (subscriptionMap.containsKey(new Pair<>(subscriber, dest))) {
            subscriptionMap.remove(new Pair<>(subscriber, dest)).unsubscribe();
        }

        var sub = session.subscribe(dest, new StompFrameHandler() {
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

        subscriptionMap.put(new Pair<>(subscriber, dest), sub);
        return sub;
    }

    private void send(String dest, Object o) {
        session.send(dest, o);
    }

    public void addTaskList(TaskList taskList, Board board) {
        send("/app/taskList/add/" + board.getId(), taskList);
    }
    public void deleteTaskList(TaskList taskList) {
        send("/app/taskList/delete/" + taskList.getId(), taskList);
    }

    public void updateTaskList(TaskList taskList, String title) {
        send("/app/taskList/update/" + taskList.getId() + "/" + title, taskList);
    }
}
