package com.softserve.teachua.dto.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.dto.city.CityResponse;
import com.softserve.teachua.utils.validations.CheckRussian;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationResponse {
    private Long id;
    @NotBlank
    @CheckRussian
    private String name;

    @NotBlank
    @CheckRussian
    private String address;
    @NotBlank
    @CheckRussian
    private String cityName;
    @NotBlank
    @CheckRussian
    private String districtName;
    @NotBlank
    @CheckRussian
    private String stationName;

    private CityResponse locationCity;

    private Long cityId;
    private Long districtId;
    private Long stationId;
    private Long clubId;

    @NotBlank(message = "coordinates cannot be null or empty")
    @NotNull(message = "coordinates cannot be null or empty")
    private String coordinates;
    private Double longitude;
    private Double latitude;

    @Pattern(regexp = "0[\\d]{9}", message = "number should contain 10 digits start with 0")
    private String phone;
}
