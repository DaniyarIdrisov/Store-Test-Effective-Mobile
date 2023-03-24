package daniyar.idrisov.test.storeservice.models.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductUpdateDTO {

    private String name;

    private String description;

    private Double price;

    private Map<String, String> characteristics;

    private List<String> keywords;

    private Long quantity;

}
