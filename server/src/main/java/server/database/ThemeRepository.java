package server.database;

import org.springframework.data.jpa.repository.JpaRepository;

import commons.Theme;

public interface ThemeRepository extends JpaRepository<Theme, Long> {}