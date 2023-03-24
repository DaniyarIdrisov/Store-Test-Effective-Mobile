package daniyar.idrisov.test.storeservice.models.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Data
public class ProductCreateDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private Double price;

    private Map<String, String> characteristics;

    private List<String> keywords;

}
