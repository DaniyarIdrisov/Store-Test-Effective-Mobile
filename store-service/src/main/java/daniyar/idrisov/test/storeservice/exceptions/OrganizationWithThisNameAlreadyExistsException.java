package daniyar.idrisov.test.storeservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "ORGANIZATION_WITH_THIS_NAME_ALREADY_EXISTS")
public class OrganizationWithThisNameAlreadyExistsException extends RuntimeException {
}
