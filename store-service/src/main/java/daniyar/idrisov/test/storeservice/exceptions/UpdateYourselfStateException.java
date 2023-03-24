package daniyar.idrisov.test.storeservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "YOU_CANNOT_UPDATE_YOURSELF_STATE")
public class UpdateYourselfStateException extends RuntimeException {
}
