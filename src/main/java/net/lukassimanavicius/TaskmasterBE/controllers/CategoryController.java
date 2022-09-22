package net.lukassimanavicius.TaskmasterBE.controllers;

import net.lukassimanavicius.TaskmasterBE.dto.CategoryDTO;
import net.lukassimanavicius.TaskmasterBE.entities.Category;
import net.lukassimanavicius.TaskmasterBE.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Return a list of all categories
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {

        // get a list of category entities
        List<Category> categories = categoryService.getAllCategories();

        // convert entities to DTO
        List<CategoryDTO> categoriesDTO = new ArrayList<>();
        for (Category cat : categories) {
            categoriesDTO.add(modelMapper.map(cat, CategoryDTO.class));
        }

        // return a list of DTO objects as a response entity
        return new ResponseEntity<>(categoriesDTO, HttpStatus.OK);
    }

    /**
     * Return a CategoryDTO according to the requested ID
     */
    @GetMapping("{id}")
    @ResponseBody
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable int id) {

        // get the category entity
        Category category = categoryService.getSingleCategory(id);

        // convert entity to DTO
        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

        // return the DTO as a response entity
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }
}
