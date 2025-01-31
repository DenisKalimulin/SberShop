package ru.kalimulin.mappers.categoryMapper;

import org.springframework.stereotype.Component;
import ru.kalimulin.entity_dto.categoryDTO.CategoryCreateDTO;
import ru.kalimulin.entity_dto.categoryDTO.CategoryResponseDTO;
import ru.kalimulin.models.Category;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapperImpl implements CategoryMapper {
    @Override
    public CategoryResponseDTO toCategoryResponseDTO(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    @Override
    public Category toCategory(CategoryCreateDTO categoryCreateDTO) {
        if (categoryCreateDTO == null) {
            return null;
        }

        return Category.builder()
                .name(categoryCreateDTO.getName())
                .build();
    }

    @Override
    public List<CategoryResponseDTO> toCategoryResponseDTOList(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return List.of();
        }

        return categories.stream()
                .map(this::toCategoryResponseDTO)
                .collect(Collectors.toList());
    }
}