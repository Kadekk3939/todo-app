package io.jmakowiecki.logic;

import io.jmakowiecki.TaskConfigurationProperties;
import io.jmakowiecki.model.*;
import io.jmakowiecki.model.projection.GroupReadModel;
import io.jmakowiecki.model.projection.GroupWriteModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;

    public ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public void saveProject(final Project toSave) {
        repository.save(toSave);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed.");
        }
        TaskGroup result = repository.findById(projectId)
                .map(project -> {
                    var targetResult = new TaskGroup();
                    targetResult.setDescription(project.getDescription());
                    targetResult.setTasks(project.getSteps().stream()
                                        .map(projectStep ->
                                            new Task(
                                                    projectStep.getDescription(),
                                                    deadline.plusDays(projectStep.getDaysToDeadline()))                                                    )
                                            .collect(Collectors.toSet())
                                        );
                    return targetResult;
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(result);
    }
}
