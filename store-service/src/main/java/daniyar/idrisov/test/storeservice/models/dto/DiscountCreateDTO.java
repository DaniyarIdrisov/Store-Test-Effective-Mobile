package daniyar.idrisov.test.storeservice.models.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class DiscountCreateDTO {

    private Double discountAmount;

    private Instant validUntil;

    private List<Long> productIds;

}
