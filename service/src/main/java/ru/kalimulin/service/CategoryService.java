package ru.kalimulin.service;


import ru.kalimulin.entity_dto.categoryDTO.CategoryCreateDTO;
import ru.kalimulin.entity_dto.categoryDTO.CategoryResponseDTO;
import ru.kalimulin.entity_dto.categoryDTO.CategoryUpdateDTO;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO findById(Long id);

    List<CategoryResponseDTO> findAll();

    CategoryResponseDTO findByName(String name);

    CategoryResponseDTO createCategory(CategoryCreateDTO categoryCreateDTO);

    CategoryResponseDTO updateCategory(CategoryUpdateDTO categoryUpdateDTO);

    //List<ProductResponseDTO> getProductsByCategoryName(String categoryName);

    void deleteCategory(Long id);
}
