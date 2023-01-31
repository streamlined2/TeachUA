package com.softserve.teachua.dto.station;

import com.softserve.teachua.dto.marker.Convertible;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StationProfile implements Convertible {
    @NotEmpty(message = "Не може бути пустим")
    private String name;

    @NotEmpty(message = "Не може бути пустим")
    private String cityName;

    @NotEmpty(message = "Не може бути пустим")
    private String districtName;
}
