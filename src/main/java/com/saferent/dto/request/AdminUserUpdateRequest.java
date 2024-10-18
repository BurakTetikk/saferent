package com.saferent.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserUpdateRequest {
    @Size(max = 50)
    @NotBlank(message = "Please provide your Firstname!!") // charSequence.toString().trim().length()>0 --> field STring ise NotNull a gerek yok
    private String firstName;

    @Size(max = 50)
    @NotBlank(message = "Please provide your Lastname!!")
    private String lastName;

    private String password;

    @Size(min = 5, max = 80)
    @Email(message = "Please provide valid e-mail!!")
    private String email;

    @Pattern(regexp = "^\\(\\d{3}\\) \\d{3}-\\d{4}$",
            message = "Please provide valid phone number!!")
    @Size(min = 14, max = 14)
    @NotBlank(message = "Please provide your phone number!!")
    private String phoneNumber;


    @Size(max = 100)
    @NotBlank(message = "Please provide your Address!!")
    private String address;

    @Size(max = 15)
    @NotBlank(message = "Please provide your Zip Code!!")
    private String zipCode;

    private Boolean builtIn;

    private Set<String> roles;
}
