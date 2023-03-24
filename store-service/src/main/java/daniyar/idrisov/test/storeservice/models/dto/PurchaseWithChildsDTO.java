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
public class PurchaseWithChildsDTO {

    private Long id;
    private Instant createdAt;
    private Double totalCost;

    private UserDTO user;

    private ProductDTO product;

    private boolean possibleToReturn;

    private boolean returned;

}
