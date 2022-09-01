package net.lukassimanavicius.TaskmasterBE;

import net.lukassimanavicius.TaskmasterBE.dto.CategoryDTO;
import net.lukassimanavicius.TaskmasterBE.entities.Category;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConvertingDTOTest {

    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    public void convertCategoryToDTOTest(){
        Category category = new Category();
        String name = "TestTest";
        category.setCategoryName(name);

        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
        assertEquals(name, category.getCategoryName());
        assertEquals(category.getCategoryName(), categoryDTO.getCategoryName());
    }

    @Test
    public void convertDTOToCategoryTest(){
        Category category = new Category();
        String name = "TestTest";
        category.setCategoryName(name);

        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

        assertEquals(name, categoryDTO.getCategoryName());
        assertEquals(categoryDTO.getCategoryName(), category.getCategoryName());
    }
}
