package daniyar.idrisov.test.storeservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "PURCHASE_ALREADY_RETURNED")
public class PurchaseAlreadyReturned extends RuntimeException {
}
