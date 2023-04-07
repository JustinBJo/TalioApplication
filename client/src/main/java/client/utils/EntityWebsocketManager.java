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

    private final Map<String, StompSession.Subscription> subscriptionMap =
            new HashMap<>();

    /**
     * @param websocket WebsocketUtils instance used in application
     * @param entityName string representing entity name used in endpoints
     * @param entityClass class returned by websocket messages
     * @param setEntity consumer that updates to a new entity
     *                  once message is received
     */
    public EntityWebsocketManager(WebsocketUtils websocket,
                                  String entityName,
                                  Class<T> entityClass,
                                  Consumer<T> setEntity) {
        this.websocket = websocket;
        this.setEntity = setEntity;
        this.entityClass = entityClass;
        this.entityName = entityName;
    }


    /**
     * Registers for websocket updates
     * @param entityId id of entity whose updates are watched
     * @param topicName watched endpoint topic
     */
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

    /**
     * getter for the current websocket
     * @return the websocket used
     */
    public WebsocketUtils getWebsocket() {
        return websocket;
    }

    /**
     * getter for the consumer instance
     * @return the Consumer instance
     */
    public Consumer<T> getSetEntity() {
        return setEntity;
    }

    /**
     * getter for the class of the entity
     * @return the class of the entity
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * getter for the name of the entity
     * @return the entity name
     */
    public String getEntityName() {
        return entityName;
    }
}
