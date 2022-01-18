package io.jmakowiecki.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    boolean existsById(Integer id);

    Optional<Task> findById(Integer id);

    List<Task> findByDone(boolean done);

    Task save(Task entity);
}
