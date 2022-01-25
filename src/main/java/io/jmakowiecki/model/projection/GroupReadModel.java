package io.jmakowiecki.model.projection;

import io.jmakowiecki.model.Task;
import io.jmakowiecki.model.TaskGroup;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;

public class GroupReadModel {
    private int id;
    @NotBlank(message = "Task group's description must not be blank!")
    private String description;
    // deadline from the latest task
    private LocalDateTime deadline;
    private List<GroupTaskReadModel> tasks;

    public GroupReadModel(TaskGroup source) {
        id = source.getId();
        description = source.getDescription();
        source.getTasks().stream()
                .map(Task::getDeadline)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .ifPresent(date -> deadline = date);
        tasks = source.getTasks().stream()
                .map(GroupTaskReadModel::new)
                .collect(Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public List<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(List<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }
}
