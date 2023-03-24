package daniyar.idrisov.test.securityservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "USER_WITH_THIS_EMAIL_ALREADY_EXISTS")
public class UserWithThisEmailAlreadyExistsException extends RuntimeException {
}
