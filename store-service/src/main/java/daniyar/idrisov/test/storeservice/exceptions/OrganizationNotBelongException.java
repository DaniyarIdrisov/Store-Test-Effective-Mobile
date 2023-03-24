package daniyar.idrisov.test.storeservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "ORGANIZATION_DOES_NOT_BELONG_TO_YOU")
public class OrganizationNotBelongException extends RuntimeException{
}
