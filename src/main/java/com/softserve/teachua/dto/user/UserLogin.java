package com.softserve.teachua.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Data
public class UserLogin {
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}