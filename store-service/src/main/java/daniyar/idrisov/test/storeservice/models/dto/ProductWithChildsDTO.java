package daniyar.idrisov.test.storeservice.models.dto;

import daniyar.idrisov.test.storeservice.models.enumerated.ProductState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithChildsDTO {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Long quantity;

    private Map<String,String> characteristics;

    private ProductState state;

    private List<String> keywords;

    private OrganizationDTO organization;

    private List<DiscountDTO> discounts;

    private List<ReviewDTO> reviews;

}
