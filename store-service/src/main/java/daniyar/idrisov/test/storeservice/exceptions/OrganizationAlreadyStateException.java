package daniyar.idrisov.test.storeservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "THIS_ORGANIZATION_ALREADY_HAS_THIS_STATE")
public class OrganizationAlreadyStateException extends RuntimeException {
}
