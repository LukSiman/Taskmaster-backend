package net.lukassimanavicius.TaskmasterBE.services;

import net.lukassimanavicius.TaskmasterBE.entities.Category;
import net.lukassimanavicius.TaskmasterBE.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    /**Returns the category with specified id*/
    public Category getCategory(int id){
        Category category = categoryRepository.getReferenceById(id);
        return category;
    }
}
