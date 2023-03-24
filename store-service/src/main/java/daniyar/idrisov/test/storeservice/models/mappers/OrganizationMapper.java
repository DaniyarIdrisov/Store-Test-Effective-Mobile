package daniyar.idrisov.test.storeservice.models.mappers;

import daniyar.idrisov.test.storeservice.models.dto.OrganizationDTO;
import daniyar.idrisov.test.storeservice.models.dto.OrganizationWithChildsDTO;
import daniyar.idrisov.test.storeservice.models.jpa.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ProductMapper.class, UserMapper.class})
public interface OrganizationMapper {

    OrganizationDTO toOrganizationDTO(Organization organization);

    OrganizationWithChildsDTO toOrganizationWithChildsDTO(Organization organization);

}
