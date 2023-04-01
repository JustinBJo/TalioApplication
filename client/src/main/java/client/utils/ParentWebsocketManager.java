package client.utils;

import client.scenes.IEntityRepresentation;
import commons.IEntity;
import org.springframework.messaging.simp.stomp.StompSession;

public class ParentWebsocketManager<T extends IEntity, C extends IEntityRepresentation<T>> {
    private final WebsocketUtils websocket;
    private final ChildrenManager<T, C> childrenManager;
    private final String childEntityName;
    private final Class<T> childEntityClass;

    private StompSession.Subscription addSub;
    private StompSession.Subscription deleteSub;

    public ParentWebsocketManager(WebsocketUtils websocket,
                                  String childEntityName,
                                  Class<T> childEntityClass,
                                  ChildrenManager<T, C> childrenManager) {
        this.websocket = websocket;
        this.childrenManager = childrenManager;
        this.childEntityName = childEntityName;
        this.childEntityClass = childEntityClass;
    }


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
}
