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

- Example:
  [DefaultBoardsService](server/src/main/java/server/service/DefaultBoardService.java)
```java
@Service
public class DefaultBoardService {
```


### JavaFX

Application uses JavaFX for the client and makes good use of available features (use of buttons/images/lists/formatting/…). The connected JavaFX controllers are used with dependency injection.

- *Excellent:* The JavaFX controllers are used with dependency injection.
  [AddTaskCtrl] client/src/main/java/client/scenes/AddTaskCtrl.java
```java
    @FXML
    private TextField title;
    @FXML
    private TextField description;
@Inject
    public AddTaskCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
    
```

[AddTitledEntityCtrl] client/src/main/java/client/scenes/AddTitledEntity.java
```java
    @FXML
    Button cancel;
    @FXML
    Button confirm;
    @FXML
    TextField textField;
    @FXML
    Label header;
@Inject
    public AddTitledEntityCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
```

[BoardCtrl] client/src/main/java/client/scenes/BoardCtrl.java
```java
    @FXML
    Label boardName;
    @FXML
    Button access;
    @FXML
    Button leave;
@Inject
    public BoardCtrl(MainCtrlTalio mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
```
[CardCtrl] client/src/main/java/client/scenes/CardCtrl.java
```java
    @FXML
    Label title;
    @FXML
    Button delete;
    @FXML
    Button edit;
    @FXML
    ImageView editIcon;
@Inject
    public CardCtrl(ServerUtils server,
                    MainCtrlTalio mainCtrlTalio) {
        this.server = server;
        this.mainCtrl = mainCtrlTalio;
    }
```
[ConnectScreenCtrl] client/src/main/java/client/scenes/CardCtrl.java
```java
    @FXML
    private TextField address;

    @FXML
    private Label notification;
@Inject
    public ConnectScreenCtrl(MainCtrlTalio mainCtrl) {
        this.mainCtrl = mainCtrl;
    }
```
[EditTaskCtrl] client/src/main/java/client/scenes/EditTaskCtrl.java
```java
    @FXML
    private TextField newTitle;
    @FXML
    private TextField newDescription;
    @FXML
    private Label currentTitle;
    @FXML
    private Label currentDescription;
@Inject
    public EditTaskCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
```
[JoinBoardCtrl] client/src/main/java/client/scenes/JoinBoardCtrl.java
```java
    @FXML
    TextField code;
@Inject
    public JoinBoardCtrl(MainCtrlTalio mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
```

[MainSceneCtrl] client/src/main/java/client/scenes/MainSceneCtrl.java
```java
    @FXML
    ImageView menuIcon;
    @FXML
    ImageView adminIcon;
    @FXML
    ImageView copyIcon;
    @FXML
    Label boardCode;
    @FXML
    Label serverAddr;
@Inject
    public MainSceneCtrl(ServerUtils server, MainCtrlTalio mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;

        this.defaultBoardID = server.getDefaultId();
    }
```
[BuildUtils] client/src/main/java/client/utils/BuildUtils.java
```java
public static <T> Pair<T, Parent> loadFXML(
            Class<T> c,
            String fxmlFileName
    ) {
        return FXML.load(c, "client", "scenes", fxmlFileName);
    }
```

[ChildrenManager] client/src/main/java/client/utils/ChildrenManager.java

```java
 var loadedChild =
                    BuildUtils.loadFXML(childSceneCtrl, childFxmlFileName);
```


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
    

