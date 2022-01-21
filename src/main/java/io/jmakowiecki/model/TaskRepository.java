package io.jmakowiecki.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    Optional<Task> findById(Integer id);

    List<Task> findAllByGroup_Id(Integer groupId);

    List<Task> findByDone(boolean done);

    Task save(Task entity);

    List<Task> findAllByDeadlineIsNull();

    List<Task> findAllByDeadlineLessThanOrderByDeadline(LocalDateTime date);
}
