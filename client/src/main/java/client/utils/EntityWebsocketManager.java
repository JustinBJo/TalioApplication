package client.utils;

import commons.IEntity;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.function.Consumer;

public class EntityWebsocketManager<T extends IEntity> {
    private final WebsocketUtils websocket;
    private final Consumer<T> setEntity;
    private final Class<T> entityClass;
    private final String entityName;

    private StompSession.Subscription subscription;

    public EntityWebsocketManager(WebsocketUtils websocket,
                                  String entityName,
                                  Class<T> entityClass,
                                  Consumer<T> setEntity) {
        this.websocket = websocket;
        this.setEntity = setEntity;
        this.entityClass = entityClass;
        this.entityName = entityName;
    }


    public void register(long entityId) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        subscription = websocket.registerForMessages(
                "/topic/" + entityName + "/update/" + entityId,
                entityClass,
                setEntity
        );
    }
}
