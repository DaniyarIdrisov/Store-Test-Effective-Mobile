package daniyar.idrisov.test.storeservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "PURCHASE_NOT_FOUND")
public class PurchaseNotFoundException extends RuntimeException{
}
