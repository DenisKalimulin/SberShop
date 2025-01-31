package ru.kalimulin.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kalimulin.entity_dto.categoryDTO.CategoryResponseDTO;
import ru.kalimulin.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/shop/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Получить список всех категорий", description = "Возвращает список всех доступных категорий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос, возвращает список категорий"),
            @ApiResponse(responseCode = "204", description = "Нет доступных категорий")
    })
    @GetMapping()
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        logger.info("Запрос всех категорий");
        List<CategoryResponseDTO> categories = categoryService.findAll();

        if (categories.isEmpty()) {
            logger.info("В базе данных нет категорий");
        } else {
            logger.info("Запрос всех категорий: {} категорий найдено", categories.size());
        }

        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Получить категорию по id", description = "Возвращает одну категорию по ее id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория найдена"),
            @ApiResponse(responseCode = "404", description = "Категория с указанным ID не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @Parameter(description = "ID категории", example = "1") @PathVariable Long id) {
        logger.info("Запрос категории по ID {}", id);
        CategoryResponseDTO category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Получить категорию по названию", description = "Возвращает категорию по ее названию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория найдена"),
            @ApiResponse(responseCode = "404", description = "Категория с указанным названием не найдена")
    })
    @GetMapping("/name")
    public ResponseEntity<CategoryResponseDTO> getCategoryByName(
            @Parameter(description = "Название категории", example = "Электроника") @RequestParam String name) {
        logger.info("Запрос по названию {}", name);
        CategoryResponseDTO category = categoryService.findByName(name);
        return ResponseEntity.ok(category);
    }
}