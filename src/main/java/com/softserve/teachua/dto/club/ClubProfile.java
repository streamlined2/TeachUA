package com.softserve.teachua.dto.club;

import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
public class ClubProfile implements Convertible {

    private Long id;

    private List<String> categoriesName;

    @Valid
    private List<LocationProfile> locations;

    private String description;

    private String name;

    @Min(2)
    @Max(17)
    private Integer ageFrom;

    @Min(3)
    @Max(18)
    private Integer ageTo;

    private String urlBackground;

    private String urlLogo;

    private Boolean isOnline;

    private String contacts;
  
    private Boolean isApproved;

    private Long userId;

    private Long centerId;

    private Long clubExternalId;

    private Long centerExternalId;
}
