package server.api;

import org.junit.jupiter.api.Test;
import server.service.DefaultBoardService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultBoardServiceTest {

    private DefaultBoardService service;

    @Test
    void getDefaultIdTest() {
        service = new DefaultBoardService();
        long res = service.getDefaultId();
        assertEquals(res, 1030);
    }
}
