package ru.kalimulin.entity_dto.listingDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kalimulin.util.ListingStatus;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingUpdateDTO {
    @NotBlank(message = "Название товара не может быть пустым")
    private String title;


    @NotBlank(message = "Описание товара не может быть пустым")
    private String description;

    @NotNull(message = "Цена не может быть пустой")
    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
    private BigDecimal price;

    private String brand;

    private String imageUrl;

    @NotNull(message = "ID категории не может быть пустым")
    private Long category;
}
