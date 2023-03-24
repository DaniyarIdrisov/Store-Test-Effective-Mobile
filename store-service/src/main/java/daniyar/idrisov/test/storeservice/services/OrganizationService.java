package daniyar.idrisov.test.storeservice.services;

import daniyar.idrisov.test.storeservice.exceptions.*;
import daniyar.idrisov.test.storeservice.models.dto.OrganizationDTO;
import daniyar.idrisov.test.storeservice.models.dto.OrganizationWithChildsDTO;
import daniyar.idrisov.test.storeservice.models.enumerated.OrganizationState;
import daniyar.idrisov.test.storeservice.models.jpa.Organization;
import daniyar.idrisov.test.storeservice.models.mappers.OrganizationMapper;
import daniyar.idrisov.test.storeservice.repositories.OrganizationRepository;
import daniyar.idrisov.test.storeservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final MinioService minioService;
    private final OrganizationMapper organizationMapper;
    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional
    public OrganizationDTO createOrganization(String name, String description, MultipartFile file) {
        checkForExisting(name);
        String filename = minioService.uploadFileAndGetFilename(file);
        Organization newOrganization = Organization.builder()
                .name(name)
                .description(description)
                .fileName(filename)
                .state(OrganizationState.CREATED)
                .createdBy(userService.getCurrentUser())
                .build();
        Organization organization = organizationRepository.save(newOrganization);
        return organizationMapper.toOrganizationDTO(organization);
    }

    private void checkForExisting(String name) {
        Optional<Organization> organization = organizationRepository.findByName(name);
        if (organization.isPresent() && !organization.get().getState().equals(OrganizationState.BANNED)) {
            throw new OrganizationWithThisNameAlreadyExistsException();
        }
    }

    @Transactional
    public List<OrganizationWithChildsDTO> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(organizationMapper::toOrganizationWithChildsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OrganizationWithChildsDTO> getAllActiveOrganizations() {
        return organizationRepository.findAllByState(OrganizationState.ACTIVE).stream()
                .map(organizationMapper::toOrganizationWithChildsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OrganizationWithChildsDTO> getAllCurrentOrganizations() {
        return userService.getCurrentUser().getOrganizations().stream()
                .map(organizationMapper::toOrganizationWithChildsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrganizationWithChildsDTO getOrganizationById(Long organizationId) {
        Organization organization =  organizationRepository.findById(organizationId)
                .orElseThrow(OrganizationNotFoundException::new);
        checkForAvailability(organization.getState());
        return organizationMapper.toOrganizationWithChildsDTO(organization);
    }

    private void checkForAvailability(OrganizationState state) {
        if (!state.equals(OrganizationState.ACTIVE) && !userService.isAdmin()) {
            throw new OrganizationUnavailableException();
        }
    }

    @SneakyThrows
    @Transactional
    public byte[] getLogo(String fileName) {
        InputStream inputStream =  minioService.getObject(fileName);
        return IOUtils.toByteArray(inputStream);
    }

    @Transactional
    public OrganizationDTO activateOrganization(Long organizationId) {
        Organization organizationForUpdate = organizationRepository.findById(organizationId)
                        .orElseThrow(OrganizationNotFoundException::new);
        checkForAlreadyState(organizationForUpdate.getState(), OrganizationState.ACTIVE);
        organizationForUpdate.setState(OrganizationState.ACTIVE);
        Organization organization = organizationRepository.save(organizationForUpdate);
        return organizationMapper.toOrganizationDTO(organization);
    }

    @Transactional
    public OrganizationDTO freezeOrganization(Long organizationId) {
        Organization organizationForUpdate = organizationRepository.findById(organizationId)
                .orElseThrow(OrganizationNotFoundException::new) ;
        checkForAlreadyState(organizationForUpdate.getState(), OrganizationState.FROZEN);
        organizationForUpdate.setState(OrganizationState.FROZEN);
        Organization organization = organizationRepository.save(organizationForUpdate);
        return organizationMapper.toOrganizationDTO(organization);
    }

    @Transactional
    public OrganizationDTO banOrganization(Long organizationId) {
        Organization organizationForUpdate = organizationRepository.findById(organizationId)
                .orElseThrow(OrganizationNotFoundException::new) ;
        checkForAlreadyState(organizationForUpdate.getState(), OrganizationState.BANNED);
        organizationForUpdate.setState(OrganizationState.BANNED);
        Organization organization = organizationRepository.save(organizationForUpdate);
        return organizationMapper.toOrganizationDTO(organization);
    }

    private void checkForAlreadyState(OrganizationState currentState, OrganizationState state) {
        if (currentState.equals(state)) {
            throw new OrganizationAlreadyStateException();
        }
    }

    @Transactional
    public List<OrganizationWithChildsDTO> getOrganizationsByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return organizationRepository.findAllByCreatedById(userId).stream()
                .map(organizationMapper::toOrganizationWithChildsDTO)
                .collect(Collectors.toList());
    }

}
