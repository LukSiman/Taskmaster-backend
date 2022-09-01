package net.lukassimanavicius.TaskmasterBE.repositories;

import net.lukassimanavicius.TaskmasterBE.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
