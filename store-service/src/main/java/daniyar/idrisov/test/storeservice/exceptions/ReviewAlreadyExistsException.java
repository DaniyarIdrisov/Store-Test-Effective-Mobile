package daniyar.idrisov.test.storeservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "REVIEW_ALREADY_EXISTS")
public class ReviewAlreadyExistsException extends RuntimeException{
}
