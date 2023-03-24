package daniyar.idrisov.test.securityservice.models.mappers;

import daniyar.idrisov.test.securityservice.models.dto.UserDTO;
import daniyar.idrisov.test.securityservice.models.jpa.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDTO toUserDto(User user);

}
