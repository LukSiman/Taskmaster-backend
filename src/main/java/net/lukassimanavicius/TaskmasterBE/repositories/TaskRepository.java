package net.lukassimanavicius.TaskmasterBE.repositories;

import net.lukassimanavicius.TaskmasterBE.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
