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
public class PurchaseDTO {

    private Long id;
    private Instant createdAt;
    private Double totalCost;
    private boolean possibleToReturn;

    private boolean returned;

}
