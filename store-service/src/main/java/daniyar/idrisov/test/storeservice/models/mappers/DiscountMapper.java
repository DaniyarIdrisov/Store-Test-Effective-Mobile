package daniyar.idrisov.test.storeservice.models.mappers;

import daniyar.idrisov.test.storeservice.models.dto.DiscountDTO;
import daniyar.idrisov.test.storeservice.models.dto.DiscountWithChildsDTO;
import daniyar.idrisov.test.storeservice.models.jpa.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ProductMapper.class})
public interface DiscountMapper {

    DiscountDTO toDiscountDTO(Discount discount);

    DiscountWithChildsDTO toDiscountWithChildsDTO(Discount discount);

}
