package daniyar.idrisov.test.storeservice.controllers;

import daniyar.idrisov.test.storeservice.models.dto.OrganizationDTO;
import daniyar.idrisov.test.storeservice.models.dto.OrganizationWithChildsDTO;
import daniyar.idrisov.test.storeservice.services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final OrganizationService service;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public OrganizationDTO createOrganization(@RequestParam("name") String name,
                                              @RequestParam("description") String description,
                                              @RequestParam("upload_file") MultipartFile file) {
        return service.createOrganization(name, description, file);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<OrganizationWithChildsDTO> getAllOrganizations() {
        return service.getAllOrganizations();
    }

    @PermitAll
    @GetMapping("/active")
    public List<OrganizationWithChildsDTO> getAllActiveOrganizations() {
        return service.getAllActiveOrganizations();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current")
    public List<OrganizationWithChildsDTO> getAllCurrentOrganizations() {
        return service.getAllCurrentOrganizations();
    }

    @PermitAll
    @GetMapping("/{id}")
    public OrganizationWithChildsDTO getOrganizationById(@PathVariable("id") Long organizationId) {
        return service.getOrganizationById(organizationId);
    }

    @PermitAll
    @GetMapping ("/logo")
    public ResponseEntity<byte[]> getLogo(@RequestParam("file_name") String fileName) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.builder("attachment").filename(fileName).build().toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(service.getLogo(fileName));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/activate")
    public OrganizationDTO activateOrganization(@PathVariable("id") Long organizationId) {
        return service.activateOrganization(organizationId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/freeze")
    public OrganizationDTO freezeOrganization(@PathVariable("id") Long organizationId) {
        return service.freezeOrganization(organizationId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/ban")
    public OrganizationDTO banOrganization(@PathVariable("id") Long organizationId) {
        return service.banOrganization(organizationId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/user")
    public List<OrganizationWithChildsDTO> getOrganizationsByUserId(@PathVariable("id") Long userId) {
        return service.getOrganizationsByUserId(userId);
    }

}
