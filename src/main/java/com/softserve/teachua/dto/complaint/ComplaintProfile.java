package com.softserve.teachua.dto.complaint;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.utils.validations.CheckRussian;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ComplaintProfile implements Convertible {
    private Long id;

    @NotBlank
    @CheckRussian
    @Length(min = 30, max = 1500, message = "length should be between 30 and 1500 symbols")
    private String text;

    @NotNull(message = "cannot be null")
    private Long userId;

    @NotNull(message = "cannot be null")
    private Long clubId;
}
