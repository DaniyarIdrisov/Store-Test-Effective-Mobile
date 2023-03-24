package daniyar.idrisov.test.storeservice.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "EVALUATION_IS_INCORRECT")
public class EvaluationIsIncorrectException extends RuntimeException {
}
