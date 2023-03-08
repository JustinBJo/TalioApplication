package server.api;

import commons.Theme;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ThemeRepository;

import java.util.*;
import java.util.function.Function;

public class TestThemeRepository implements ThemeRepository {

    public final Map<Long,Theme> themes = new HashMap<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }


    @Override
    public List<Theme> findAll() {
        return null;
    }

    @Override
    public List<Theme> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Theme> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Theme> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Theme entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Theme> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Theme> S save(S entity) {
        call("save");
        themes.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends Theme> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Theme> findById(Long aLong) {
        call("findById");
        if(themes.containsKey(aLong)) {
            return Optional.of(themes.get(aLong));
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Theme> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Theme> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Theme> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Theme getOne(Long aLong) {
        return null;
    }

    @Override
    public Theme getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Theme> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Theme> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Theme> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Theme> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Theme> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Theme> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Theme, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
