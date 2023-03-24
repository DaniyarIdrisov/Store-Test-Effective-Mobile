package daniyar.idrisov.test.storeservice.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "JWT_TOKEN_IS_EXPIRED_OR_INVALID")
public class JwtAuthenticationException extends RuntimeException {
}
