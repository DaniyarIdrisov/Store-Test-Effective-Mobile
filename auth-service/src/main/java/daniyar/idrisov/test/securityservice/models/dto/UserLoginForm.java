package daniyar.idrisov.test.securityservice.models.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserLoginForm {

    @Email
    private String email;

    @NotBlank
    private String password;

}
