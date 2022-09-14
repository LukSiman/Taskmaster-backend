package net.lukassimanavicius.TaskmasterBE.repositories;

import net.lukassimanavicius.TaskmasterBE.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Task findByTaskUUID(UUID taskUUID);

    String deleteByTaskUUID(UUID taskUUID);
}
