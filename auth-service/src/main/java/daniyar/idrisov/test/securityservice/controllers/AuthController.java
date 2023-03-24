package daniyar.idrisov.test.securityservice.controllers;
;
import daniyar.idrisov.test.securityservice.models.dto.TokenDTO;
import daniyar.idrisov.test.securityservice.models.dto.UserDTO;
import daniyar.idrisov.test.securityservice.models.dto.UserLoginForm;
import daniyar.idrisov.test.securityservice.models.dto.UserRegistrationForm;
import daniyar.idrisov.test.securityservice.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService service;

    @PermitAll
    @Operation(summary = "Регистрация")
    @PostMapping("/registration")
    public UserDTO registration(@Valid @RequestBody UserRegistrationForm form) {
        return service.registration(form);
    }

    @PermitAll
    @Operation(summary = "Авторизация")
    @PostMapping("/login")
    public TokenDTO login(@Valid @RequestBody UserLoginForm form) {
        return service.login(form);
    }

}
