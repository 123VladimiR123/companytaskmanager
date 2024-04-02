package com.spring.companytaskmanager.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Validated
public class UserDTO {

    @Email
    @NotEmpty
    private final String username;
    @NotEmpty
    private final String firstName;
    @NotEmpty
    private final String lastName;
    private final String fatherName;
    @NotEmpty
    @Pattern(regexp = "^[0-9]{10}$")
    private final String phoneNumber;
    @NotEmpty
    private final String departmentName;
    @NotEmpty
    private final String role;
    private final LocalDate birthDate;
    private final Boolean isAdmin;
}
