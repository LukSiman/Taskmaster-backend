package net.lukassimanavicius.TaskmasterBE.repositories;

import net.lukassimanavicius.TaskmasterBE.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Task findByTaskUUID(UUID taskUUID);

    List<Task> findByTaskDate(LocalDate date);

//    List<Task> findByTaskStartTime(LocalDate date);

//    List<Task> findByTaskStartTimeStartingWith(LocalDate date);

    String deleteByTaskUUID(UUID taskUUID);
}
