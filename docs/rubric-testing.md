# Rubric: Testing

The students are not supposed to submit a specific assignment, instead, you are supposed to look into their code base. You can share this rubric with the teams and ask them for pre-filled feedback until Friday.


### Coverage

Testing is an integral part of the coding activities. Unit tests cover all parts of the application (client, server, commons). Excellent teams will also pay attention to the (unit test) code coverage of crucial system components (accordind to the build result over all components).

- Sufficient: The average MR does not only contain business logic, but also unit tests.
- Directory for unit tests:
    - commons/src/test
    - server/src/test
- MR's containing tests:
    - https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-45/-/merge_requests/55
    - https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-45/-/merge_requests/60

### Unit Testing

Classes are tested in isolation. Configurable *dependent-on-components* are passed to the *system-under-test* to avoid integration tests (for example, to avoid running a database or opening REST requests in each a test run).

- Excellent: Configurable subclasses are created to replace dependent-on-components in most of the tests.
- server/src/test/java/server/api/testRepository
- ```java
  public class TestBoardRepository implements BoardRepository {
  ```
- ```java
  public class TestSubtaskRepository implements SubtaskRepository {
  ```

### Indirection

The project applies the test patterns that have been covered in the lecture on *Dependency Injection*. More specifically, the test suite includes tests for indirect input/output and behavior.

- Excellent: The project contains tests that assert indirect input, indirect output, and behavior for multiple systems-under-test.
- server/src/test/java/server/api
- Example:
    - ```java
      private BoardRepository repo;
      private TaskListRepository listRepo;
      private DefaultBoardService service;
      private BoardController sut;


      @BeforeEach
      void setUp() {
          repo = new TestBoardRepository();
          listRepo = new TestTaskListRepository();
          service = new DefaultBoardService();
          sut = new BoardController(repo, listRepo, service);
      }
      ```


### Endpoint Testing

The REST API is tested through automated JUnit tests.

- Excellent: The project contains automated tests that cover regular and exceptional use of most endpoints.
- server/src/test/java/server/api/BoardControllerTest.java, etc
- ```java
    // Good weather case
    @Test
    void updateTest() {
        Board board = new Board("1030", "oldName");
        board.setId(2001);
        repo.save(board);

        // Update the board with a new name
        String newName = "newName";
        ResponseEntity<Board> response = sut.updateName(2001, newName);

        // Check endpoint
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(board, response.getBody());

        // Check repository
        assertTrue(repo.findAll().contains(board));
        assertEquals(repo.findById(board.getId()).get().getTitle(), newName);
    }

    // Bad weather case
    @Test
    void failedUpdateTest() {
        Board newBoard = new Board("1030",
                "This board does not exist in the repository");
        newBoard.setId(2001);

        String newName = "newName";
        ResponseEntity<Board> response = sut.updateName(2001, newName);

        // Check endpoint
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
  ```

