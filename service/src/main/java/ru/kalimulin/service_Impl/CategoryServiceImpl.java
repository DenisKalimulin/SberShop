package ru.kalimulin.service_Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kalimulin.custum_exceptions.categoryException.CategoryAlreadyExistsException;
import ru.kalimulin.custum_exceptions.categoryException.CategoryNotFoundException;
import ru.kalimulin.entity_dto.categoryDTO.CategoryCreateDTO;
import ru.kalimulin.entity_dto.categoryDTO.CategoryResponseDTO;
import ru.kalimulin.entity_dto.categoryDTO.CategoryUpdateDTO;
import ru.kalimulin.mappers.categoryMapper.CategoryMapper;
import ru.kalimulin.models.Category;
import ru.kalimulin.repositories.CategoryRepository;
import ru.kalimulin.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponseDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория c id " + id + " не найдена"));
        return categoryMapper.toCategoryResponseDTO(category);
    }

    @Override
    public CategoryResponseDTO findByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Категория c названием " + name + " не найдена"));

        return categoryMapper.toCategoryResponseDTO(category);
    }

    @Override
    public List<CategoryResponseDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toCategoryResponseDTOList(categories);
    }

    @Transactional
    @Override
    public CategoryResponseDTO createCategory(CategoryCreateDTO categoryCreateDTO) {
        if (categoryRepository.existsByName(categoryCreateDTO.getName())) {
            throw new CategoryAlreadyExistsException("Категория уже существует");
        }

        Category category = categoryMapper.toCategory(categoryCreateDTO);
        category = categoryRepository.save(category);

        return categoryMapper.toCategoryResponseDTO(category);
    }

    @Transactional
    @Override
    public CategoryResponseDTO updateCategory(CategoryUpdateDTO categoryUpdateDTO) {
        if (categoryRepository.existsByName(categoryUpdateDTO.getName())) {
            throw new CategoryAlreadyExistsException("Категория уже существует");
        }

        Category category = categoryRepository.findById(categoryUpdateDTO.getId())
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));

        if (categoryUpdateDTO.getName() != null) {
            category.setName(categoryUpdateDTO.getName());
        }

        category = categoryRepository.save(category);
        return categoryMapper.toCategoryResponseDTO(category);
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория c id " + id + " не найдена"));

        categoryRepository.delete(category);
    }
}