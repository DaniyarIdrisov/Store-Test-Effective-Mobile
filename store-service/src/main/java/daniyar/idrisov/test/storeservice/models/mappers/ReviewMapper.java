package daniyar.idrisov.test.storeservice.models.mappers;

import daniyar.idrisov.test.storeservice.models.dto.ReviewDTO;
import daniyar.idrisov.test.storeservice.models.dto.ReviewWithChildsDTO;
import daniyar.idrisov.test.storeservice.models.jpa.Review;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ProductMapper.class, UserMapper.class})
public interface ReviewMapper {

    ReviewDTO toReviewDTO(Review review);

    ReviewWithChildsDTO toReviewWithChildsDTO(Review review);

}
