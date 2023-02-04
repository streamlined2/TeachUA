package com.softserve.teachua.dto.security;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;
}
