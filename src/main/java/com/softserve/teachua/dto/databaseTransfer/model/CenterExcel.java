package com.softserve.teachua.dto.databaseTransfer.model;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.utils.validations.CheckRussian;
import com.softserve.teachua.utils.validations.Phone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CenterExcel implements Convertible {
    private Long centerExternalId;

    @NotBlank
    @Size(min = 5, max = 100, message = "Довжина назви має бути від 5 до 100 символів")
    private String name;

    @CheckRussian
    @NotEmpty
    private String description;

    @Phone
    @NotBlank
    private String phone;

    /**
     * Site field can include social media too.
     */
    private String site;
}
