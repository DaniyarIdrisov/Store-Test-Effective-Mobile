package daniyar.idrisov.test.storeservice.models.mappers;

import daniyar.idrisov.test.storeservice.models.dto.ProductDTO;
import daniyar.idrisov.test.storeservice.models.dto.ProductWithChildsDTO;
import daniyar.idrisov.test.storeservice.models.jpa.Product;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductDTO toProductDTO(Product product);

    ProductWithChildsDTO toProductWithChildsDTO(Product product);

}
