package daniyar.idrisov.test.storeservice.models.mappers;

import daniyar.idrisov.test.storeservice.models.dto.UserDTO;
import daniyar.idrisov.test.storeservice.models.dto.UserWithChildsDTO;
import daniyar.idrisov.test.storeservice.models.jpa.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDTO toUserDto(User user);

    UserWithChildsDTO toUserWithChildsDTO(User user);

}
