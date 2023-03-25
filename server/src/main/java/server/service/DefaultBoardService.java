package server.service;

import org.springframework.stereotype.Service;

@Service
public class DefaultBoardService {
    private final long defaultId = 1030;

    /**
     * Get the default board id
     * @return the default id
     */
    public long getDefaultId() {
        return defaultId;
    }
}
