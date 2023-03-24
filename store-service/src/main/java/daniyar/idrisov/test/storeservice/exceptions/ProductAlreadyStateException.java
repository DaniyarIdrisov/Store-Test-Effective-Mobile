package daniyar.idrisov.test.storeservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "THIS_PRODUCT_ALREADY_HAS_THIS_STATE")
public class ProductAlreadyStateException extends RuntimeException {
}
