package daniyar.idrisov.test.storeservice.models.dto;

import daniyar.idrisov.test.storeservice.models.enumerated.OrganizationState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationWithChildsDTO {

    private Long id;
    private String name;

    private String description;

    private String fileName;

    private OrganizationState state;

    private UserDTO createdBy;

    private List<ProductDTO> products;

}
