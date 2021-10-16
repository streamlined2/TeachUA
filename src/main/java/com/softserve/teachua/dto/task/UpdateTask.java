package com.softserve.teachua.dto.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateTask implements Convertible {
    @NotNull
    @Pattern(regexp = "^[А-Яа-яЇїІіЄєҐґa-zA-Z0-9()!\"#$%&'*+ ,-.:;<=>?|@_`{}~^&&[^ыЫъЪёЁэЭ]]+$",
            message = "Name can contains only letters of ukrainian and english languages, numbers, and some special symbols like: [\"#$%&'*+ ,-.:;<=>?|@_`{}~]")
    @Size(min = 5, max = 30, message = "Name must contain a minimum of 5 and a maximum of 30 letters")
    private String name;
    @NotNull
    @Pattern(regexp = "^[^ыЫъЪёЁэЭ]+$", message = "Description cant contain letters of russian languages")
    private String description;
    @NotNull
    @Pattern(regexp = "/\\b.+/[^/]+\\.[A-z]+", message = "Incorrect file path")
    private String picture;
    @NotNull
    private LocalDate startDate;
    private Long challengeId;
}