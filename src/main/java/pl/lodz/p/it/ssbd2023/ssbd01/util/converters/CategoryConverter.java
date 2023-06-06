package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.medication.CategoryDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;

public class CategoryConverter {

    private CategoryConverter() {}

    public static CategoryDTO mapCategoryToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .isOnPrescription(category.getIsOnPrescription())
                .name(category.getName())
                .build();
    }

}