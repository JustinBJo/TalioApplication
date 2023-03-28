# Rubric: Technology

The students are not supposed to submit a specific assignment, instead, you are supposed to look into their code base.

### Dependency Injection

Application uses dependency injection to connect dependent components. No use of static fields in classes.

- *Excellent:* The application (client and server) uses dependency injection everywhere to connect dependent components. The projects also binds some external types so they can be injected.


Client
- [BuildUtils](client/src/main/java/client/utils/BuildUtils.java)
- [MyModule](client/src/main/java/client/MyModule.java)
- Scene controllers
  - e.g. [MainSceneCtrl](client/src/main/java/client/scenes/MainSceneCtrl.java), etc
```java
  @Inject
  public MainSceneCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
      this.server = server;
      this.mainCtrl = mainCtrl;

      this.defaultBoardID = server.getDefaultId();
  }
```

Server
- Rest controllers
  - e.g. [BoardController](server/src/main/java/server/api/BoardController.java), etc
```java
  private final BoardRepository repo;
  private final TaskListRepository taskListRepo;
  private final DefaultBoardService service;

  /**
   * Constructor
   * @param repo BoardRepository
   * @param taskListRepo TaskListRepository
   */
  public BoardController(
          BoardRepository repo,
          TaskListRepository taskListRepo,
          DefaultBoardService service
  ) {
      this.repo = repo;
      this.taskListRepo = taskListRepo;
      this.service = service;
  }
```


### Spring Boot

Application makes good use of the presented Spring built-in concepts to configure the server and maintain the lifecycle of the various server components.

- *Excellent:* Additional @Services are defined, which encapsulate business logic or shared state.
- *Good:* The application contains example of @Controller, @RestController, and a JPA repository.
- *Sufficient:* The application uses Spring for the server.
- *Insufficient:* The application uses regular socket communication.


### JavaFX

Application uses JavaFX for the client and makes good use of available features (use of buttons/images/lists/formatting/…). The connected JavaFX controllers are used with dependency injection.

- *Excellent:* The JavaFX controllers are used with dependency injection.
- *Good:* The UI contains more than just buttons, text fields, or labels. The application contains images and a non-default layout.
- *Sufficient:* Application uses JavaFX for the client.
- *Insufficient:*


### Communcation

Application uses communication via REST requests and Websockets. The code is leveraging the canonical Spring techniques for endpoints and websocket that have been introduced in the lectures. The client uses libraries to simplify access.

- *Excellent:* The server defines all REST and webservice endpoints through Spring and uses a client library like Jersey (REST) or Stomp (Webservice) to simplify the server requests.
- *Good:* All communication between client and server is implemented with REST or websockets.
- *Sufficient:* The application contains functionality that uses 1) a REST request AND 2) long-polling AND 3) websocket communication (in different places).
- *Insufficient:* The application does not contain functionality that uses a REST request OR 2) long-polling, OR 3) websocket communication.


### Data Transfer

Application defines meaningful data structures and uses Jackson to perform the de-/serialization of submitted data.

- *Excellent:* Jackson is used implicitly by Spring or the client library. No explicit Jackson calls are required in the application.
- client/src/main/java/client/utils/ServerUtils.java
- Example:
  - ```java
    public List<TaskList> getBoardData(long boardId) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("board/" + boardId + "/tasklist")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<TaskList>>(){
                });
    }
    ```
    

