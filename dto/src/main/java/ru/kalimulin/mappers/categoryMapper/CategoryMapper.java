package ru.kalimulin.mappers.categoryMapper;

import ru.kalimulin.entity_dto.categoryDTO.CategoryCreateDTO;
import ru.kalimulin.entity_dto.categoryDTO.CategoryResponseDTO;
import ru.kalimulin.models.Category;

import java.util.List;

public interface CategoryMapper {
    CategoryResponseDTO toCategoryResponseDTO(Category category);

    Category toCategory(CategoryCreateDTO CategoryCreateDTO);

    List<CategoryResponseDTO> toCategoryResponseDTOList(List<Category> categories);
}
