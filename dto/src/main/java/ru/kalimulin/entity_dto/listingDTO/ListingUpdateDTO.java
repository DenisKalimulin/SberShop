package ru.kalimulin.entity_dto.listingDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.kalimulin.util.ListingStatus;

import java.math.BigDecimal;

public class ListingUpdateDTO {
    @NotNull(message = "ID не может быть пустым")
    private long id;

    @NotBlank(message = "Название товара не может быть пустым")
    private String title;

    @NotBlank(message = "Описание товара не может быть пустым")
    private String description;

    @NotNull(message = "Цена не может быть пустой")
    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
    private BigDecimal price;

    @NotNull(message = "Статус не может быть пустым")
    private ListingStatus status;

    @Size(min = 0, max = 256, message = "Название бренда не должно превышать 256 символов")
    private String brand;

    private String imageUrl;
}
