package server.api.testRepository;

import commons.Board;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestBoardRepository implements BoardRepository {

    List<Board> boards = new ArrayList<>();
    List<String> calls = new ArrayList<>();

    /**
     * Returns true if the given call was made to the repository.
     * @param tbr TestBoardRepository
     * @param call the call method name
     * @return true if the call was made
     */
    public static boolean containsCall(TestBoardRepository tbr, String call) {
        return tbr.calls.contains(call);
    }


    @Override
    public List<Board> findAll() {
        calls.add("findAll");
        return boards;
    }


    @Override
    public List<Board> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Board> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Board> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        calls.add("count");
        return boards.size();
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Board entity) {
        calls.add("delete");
        boards.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Board> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Board> S save(S entity) {
        calls.add("save");
        boards.add(entity);
        return entity;
    }

    @Override
    public <S extends Board> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Board> findById(Long aLong) {
        calls.add("findById");
        for (Board b : boards) {
            if (b.getId() == aLong) {
                return Optional.of(b);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        calls.add("existsById");
        for (Board b : boards) {
            if (b.getId() == aLong) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Board> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Board> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Board> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Board getOne(Long aLong) {
        return null;
    }

    @Override
    public Board getById(Long aLong) {
        calls.add("getById");
        var board = findById(aLong);
        if (board.isEmpty()) return null;
        return board.get();
    }

    @Override
    public <S extends Board> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Board> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Board> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Board> Page<S> findAll(Example<S> example,
                                             Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Board> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Board> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Board, R> R findBy(Example<S> example,
         Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public void updateBoardId(long oldId, long newId) {
        Board b = getById(oldId);
        if (b != null) {
            b.setId(newId);
            delete(getById(oldId));
            save(b);
        }
    }
}
