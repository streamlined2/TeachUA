package com.softserve.teachua.dto.certificateDates;

import com.softserve.teachua.dto.marker.Convertible;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class CertificateDatesProfile implements Convertible {
    private Long id;

    @NotBlank
    @Pattern(regexp = "^\\d{2}.\\d{2}.\\d{4}", message = "Неправильний формат дати видачі сертифікату.")
    private String date;

    @NotBlank
    private Integer hours;

    @NotBlank
    private String duration;

    @NotBlank
    @Pattern(regexp = "^[0-9]{2}$", message = "Неправильний формат.")
    private String courseNumber;
}
