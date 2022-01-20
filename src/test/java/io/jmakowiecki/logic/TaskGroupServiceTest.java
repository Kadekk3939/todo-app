package io.jmakowiecki.logic;

import io.jmakowiecki.model.TaskGroup;
import io.jmakowiecki.model.TaskGroupRepository;
import io.jmakowiecki.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when group has undone tasks")
    void toggleGroup_hasUndoneTasks_throwsIllegalStateException() {
        // given
        var mockTaskRepo = mock(TaskRepository.class);
        when(mockTaskRepo.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(true);
        // system under test
        var toTest = new TaskGroupService(null, mockTaskRepo);

        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("has undone tasks");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when group has not undone tasks and find returns empty")
    void toggleGroup_wrongId_throwsIllegalArgumentException() {
        // given
        var mockTaskRepo = mock(TaskRepository.class);
        when(mockTaskRepo.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        // and
        var mockTaskGroupRepo = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepo.findById(anyInt())).thenReturn(Optional.empty());
        // system under test
        var toTest = new TaskGroupService(mockTaskGroupRepo, mockTaskRepo);

        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given id not found");
    }

    @Test
    @DisplayName("should toggle group")
    void toggleGroup_worksAsExpected() {
        // given
        var group = new TaskGroup();
        var beforeToggle = group.isDone();
        var mockTaskRepo = mock(TaskRepository.class);
        when(mockTaskRepo.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        // and
        var mockTaskGroupRepo = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepo.findById(anyInt())).thenReturn(Optional.of(group));
        // system under test
        var toTest = new TaskGroupService(mockTaskGroupRepo, mockTaskRepo);

        // when
        toTest.toggleGroup(1);

        // then
        assertThat(group.isDone())
                .isEqualTo(!beforeToggle);
    }


}