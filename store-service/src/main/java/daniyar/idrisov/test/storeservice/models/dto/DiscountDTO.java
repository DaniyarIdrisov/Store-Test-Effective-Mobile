package daniyar.idrisov.test.storeservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDTO {

    private Long id;

    private Double discountAmount;

    private Instant validUntil;

}
