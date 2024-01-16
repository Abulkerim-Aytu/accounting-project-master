package com.cydeo.dto;

import lombok.*;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @NotBlank
    @Size(max = 50, min = 2)
    private String firstname;

    @NotBlank
    @Size(max = 50, min = 2)
    private String lastname;

    @NotBlank
    @Email
    private String username;

    @NotBlank
    @Pattern(regexp = "^\\d{10}$")
    private String phone;

    @NotBlank
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,}")
    private String password;

    @NotNull
    private String confirmPassword;

    @NotNull
    private RoleDto role;

    private CompanyDto company;

    private boolean isOnlyAdmin; // Note from User Story: Should be true if this user is only admin of any company
    // TO BE DONE DURING SECURITY

}