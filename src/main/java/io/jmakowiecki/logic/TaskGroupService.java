package io.jmakowiecki.logic;

import io.jmakowiecki.model.Project;
import io.jmakowiecki.model.TaskGroup;
import io.jmakowiecki.model.TaskGroupRepository;
import io.jmakowiecki.model.TaskRepository;
import io.jmakowiecki.model.projection.GroupReadModel;
import io.jmakowiecki.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

public class TaskGroupService {
    private final TaskGroupRepository repository;
    private final TaskRepository taskRepository;

    public TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    GroupReadModel createGroup(GroupWriteModel source, Project project) {
        TaskGroup result = repository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }
    public GroupReadModel createGroup(GroupWriteModel source) {
        return createGroup(source, null);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Do all the tasks first.");
        }
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }

}
