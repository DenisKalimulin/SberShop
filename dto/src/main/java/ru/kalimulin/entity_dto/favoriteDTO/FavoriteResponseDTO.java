package ru.kalimulin.entity_dto.favoriteDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteResponseDTO {
    private Long id;
    private String email;
    private List<Long> listingIds;
}
