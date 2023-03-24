package daniyar.idrisov.test.storeservice.models.mappers;

import daniyar.idrisov.test.storeservice.models.dto.PurchaseWithChildsDTO;
import daniyar.idrisov.test.storeservice.models.jpa.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ProductMapper.class, UserMapper.class})
public interface PurchaseMapper {

    PurchaseWithChildsDTO toPurchaseWithChildsDTO(Purchase purchase);

}
