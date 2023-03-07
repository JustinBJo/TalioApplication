package server.database;

import org.springframework.data.jpa.repository.JpaRepository;

import commons.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {}
