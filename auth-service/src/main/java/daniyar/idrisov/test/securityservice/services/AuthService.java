package daniyar.idrisov.test.securityservice.services;

import daniyar.idrisov.test.securityservice.constants.NumericalConstants;
import daniyar.idrisov.test.securityservice.exceptions.AccountIsNotValidException;
import daniyar.idrisov.test.securityservice.exceptions.UserWithThisEmailAlreadyExistsException;
import daniyar.idrisov.test.securityservice.models.dto.TokenDTO;
import daniyar.idrisov.test.securityservice.models.dto.UserDTO;
import daniyar.idrisov.test.securityservice.models.dto.UserLoginForm;
import daniyar.idrisov.test.securityservice.models.dto.UserRegistrationForm;
import daniyar.idrisov.test.securityservice.models.enumerated.Role;
import daniyar.idrisov.test.securityservice.models.enumerated.UserState;
import daniyar.idrisov.test.securityservice.models.jpa.User;
import daniyar.idrisov.test.securityservice.models.mappers.UserMapper;
import daniyar.idrisov.test.securityservice.repositories.UserRepository;
import daniyar.idrisov.test.securityservice.security.providers.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserDTO registration(UserRegistrationForm form) {
        checkForUserExisting(form.getEmail());
        User newUser = User.builder()
                .username(form.getUsername())
                .email(form.getEmail())
                .hashPassword(passwordEncoder.encode(form.getPassword()))
                .balance(NumericalConstants.ZERO_BALANCE)
                .role(Role.USER)
                .state(UserState.ACTIVE)
                .build();
        User user = repository.save(newUser);
        return mapper.toUserDto(user);
    }

    private void checkForUserExisting(String email) {
        if (repository.findByEmail(email).isPresent()) {
            throw new UserWithThisEmailAlreadyExistsException();
        }
    }

    @Transactional
    public TokenDTO login(UserLoginForm form) {
        try {
            User user = repository.findByEmail(form.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("INVALID_LOGIN_OR_PASSWORD"));
            checkForState(user.getState());
            if (!passwordEncoder.matches(form.getPassword(), user.getHashPassword())) {
                throw new BadCredentialsException("INVALID_LOGIN_OR_PASSWORD");
            }
            return TokenDTO.builder()
                    .email(form.getEmail())
                    .token(jwtTokenProvider.createToken(form.getEmail()))
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("INVALID_LOGIN_OR_PASSWORD");
        }
    }

    private void checkForState(UserState state) {
        if (!state.equals(UserState.ACTIVE)) {
            throw new AccountIsNotValidException();
        }
    }

}
