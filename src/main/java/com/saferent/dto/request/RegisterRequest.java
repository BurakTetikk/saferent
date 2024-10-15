package com.saferent.dto.request;

import com.saferent.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(max = 50)
    @NotBlank(message = "Please provide your Firstname!!") // charSequence.toString().trim().length()>0 --> field STring ise NotNull a gerek yok
    private String firstName;

    @Size(max = 50)
    @NotBlank(message = "Please provide your Lastname!!")
    private String lastName;

    @Size(min = 5, max = 80)
    @Email(message = "Please provide valid e-mail!!")
    private String email;

    @Size(min = 4, max = 20, message = "Please provide correct size of password!!")
    @NotBlank(message = "Please provide your Password!!")
    private String password;

    @Pattern(regexp = "^((\\(\\d{3}\\))/\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
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

}
