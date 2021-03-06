package io.jmakowiecki.model;

import io.jmakowiecki.model.event.TaskEvent;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Task's description must not be blank!")
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    @Embedded
    private Audit audit = new Audit();
    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

    Task() {
    }

    public Task(String description, LocalDateTime deadline) {
        this(description, deadline, null);
    }

    public Task(String description, LocalDateTime deadline, TaskGroup taskGroup) {
        this.deadline = deadline;
        this.description = description;
        if (taskGroup != null) {
            this.group = taskGroup;
        }
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public TaskEvent toggle() {
        this.done = !this.done;
        return TaskEvent.changed(this);
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    Audit getAudit() {
        return audit;
    }

    void setAudit(Audit audit) {
        this.audit = audit;
    }

    TaskGroup getGroup() {
        return group;
    }

    void setGroup(TaskGroup group) {
        this.group = group;
    }

    public void updateFrom (final Task source) {
        description = source.description;
        done = source.done;
        deadline = source.deadline;
        group = source.group;
    }
}
