package client.utils;

import client.scenes.IEntityRepresentation;
import commons.IEntity;
import org.springframework.messaging.simp.stomp.StompSession;

public class ParentWebsocketManager
        <T extends IEntity, C extends IEntityRepresentation<T>> {
    private final WebsocketUtils websocket;
    private final ChildrenManager<T, C> childrenManager;
    private final String childEntityName;
    private final Class<T> childEntityClass;

    private StompSession.Subscription addSub;
    private StompSession.Subscription deleteSub;

    /**
     * @param websocket WebsocketUtils instance used in application
     * @param childEntityName string representing child entity name
     *                        used in endpoints
     * @param childEntityClass class returned by websocket messages
     * @param childrenManager manager that handles update in children
     */
    public ParentWebsocketManager(WebsocketUtils websocket,
                                  String childEntityName,
                                  Class<T> childEntityClass,
                                  ChildrenManager<T, C> childrenManager) {
        this.websocket = websocket;
        this.childrenManager = childrenManager;
        this.childEntityName = childEntityName;
        this.childEntityClass = childEntityClass;
    }


    /**
     * Registers for websocket updates
     * @param entityId id of entity whose updates are watched
     */
    public void register(long entityId) {
        if (addSub != null) {
            addSub.unsubscribe();
        }
        addSub =
                websocket.registerForMessages(
                    "/topic/" + childEntityName + "/add/" + entityId,
                    childEntityClass,
                    childrenManager::addOrUpdateChild
                );

        if (deleteSub != null) {
            deleteSub.unsubscribe();
        }
        deleteSub =
                websocket.registerForMessages(
                    "/topic/" + childEntityName + "/delete",
                    childEntityClass,
                    childrenManager::removeChild
                );
    }

    /**
     * getter for the current websocket
     * @return the websocket
     */
    public WebsocketUtils getWebsocket() {
        return websocket;
    }

    /**
     * getter for the childrenManager
     * @return the childrenManager
     */
    public ChildrenManager<T, C> getChildrenManager() {
        return childrenManager;
    }

    /**
     * getter for the entity name of the child
     * @return the String entity name
     */
    public String getChildEntityName() {
        return childEntityName;
    }

    /**
     * getter for the class of the child
     * @return the Child class
     */
    public Class<T> getChildEntityClass() {
        return childEntityClass;
    }
}
