package io.jmakowiecki.logic;

import io.jmakowiecki.TaskConfigurationProperties;
import io.jmakowiecki.model.ProjectRepository;
import io.jmakowiecki.model.TaskGroupRepository;
import io.jmakowiecki.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LogicConfiguration {
    @Bean
    ProjectService projectService(
            final ProjectRepository repository,
            final TaskGroupRepository taskGroupRepository,
            final TaskConfigurationProperties config)
    {
        return new ProjectService(repository, taskGroupRepository, config);
    }

    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupRepository repository,
            final TaskRepository taskRepository)
    {
        return new TaskGroupService(repository, taskRepository);
    }
}
