package daniyar.idrisov.test.securityservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "ACCOUNT_IS_NOT_VALID")
public class AccountIsNotValidException extends RuntimeException {
}
