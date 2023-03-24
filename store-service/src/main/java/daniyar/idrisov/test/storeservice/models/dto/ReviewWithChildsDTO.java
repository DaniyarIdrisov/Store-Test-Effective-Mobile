package daniyar.idrisov.test.storeservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewWithChildsDTO {

    private Long id;

    private Double evaluation;

    private String comment;

    private ProductDTO product;

    private UserDTO createdBy;

}
