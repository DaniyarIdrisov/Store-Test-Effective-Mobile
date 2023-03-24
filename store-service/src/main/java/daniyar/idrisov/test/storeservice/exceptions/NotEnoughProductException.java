package daniyar.idrisov.test.storeservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "NOT_ENOUGH_PRODUCT_IN_STOCK")
public class NotEnoughProductException extends RuntimeException {
}
