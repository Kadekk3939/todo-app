package io.jmakowiecki.model;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {
    List<TaskGroup> findAll();

    Optional<TaskGroup> findById(Integer id);

    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);

    boolean existsByDescription(String description);

    TaskGroup save(TaskGroup entity);
}
