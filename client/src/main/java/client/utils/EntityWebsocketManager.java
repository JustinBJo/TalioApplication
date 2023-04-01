package client.utils;

import commons.IEntity;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EntityWebsocketManager<T extends IEntity> {
    private final WebsocketUtils websocket;
    private final Consumer<T> setEntity;
    private final Class<T> entityClass;
    private final String entityName;

    private final Map<String, StompSession.Subscription> subscriptionMap = new HashMap<>();

    public EntityWebsocketManager(WebsocketUtils websocket,
                                  String entityName,
                                  Class<T> entityClass,
                                  Consumer<T> setEntity) {
        this.websocket = websocket;
        this.setEntity = setEntity;
        this.entityClass = entityClass;
        this.entityName = entityName;
    }


    public void register(long entityId, String topicName) {
        if (subscriptionMap.containsKey(topicName)) {
            subscriptionMap.get(topicName).unsubscribe();
        }
        var sub =
                websocket.registerForMessages(
                    "/topic/" + entityName + "/" + topicName + "/" + entityId,
                    entityClass,
                    setEntity
                );
        subscriptionMap.put(topicName, sub);
    }
}
