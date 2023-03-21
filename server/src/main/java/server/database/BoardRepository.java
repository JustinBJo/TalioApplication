package server.database;

import commons.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    /**
     * Update the auto-generated ID of a board
     * @param oldId the id of the board
     * @param newId the new id of the board
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE Board b SET b.id = :newId " +
            "WHERE b.id = :oldId", nativeQuery = true)
    void updateBoardId(long oldId, long newId);
}