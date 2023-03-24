package daniyar.idrisov.test.storeservice.controllers;

import daniyar.idrisov.test.storeservice.models.dto.UserDTO;
import daniyar.idrisov.test.storeservice.models.dto.UserWithChildsDTO;
import daniyar.idrisov.test.storeservice.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    @ApiOperation(value = "Get all users")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully found", response = List.class)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<UserWithChildsDTO> getAllUsers() {
        return service.getAllUsers();
    }

    @ApiOperation(value = "Get user by id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully found", response = UserWithChildsDTO.class)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public UserWithChildsDTO getUserById(@PathVariable("id") Long userId) {
        return service.getUserById(userId);
    }

    @ApiOperation(value = "Get current user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully found", response = UserWithChildsDTO.class)})
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current")
    public UserWithChildsDTO getCurrentUser() {
        return service.getCurrentUserDTO();
    }

    @ApiOperation(value = "Activate user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully activated", response = UserDTO.class)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/activate")
    public UserDTO activateUser(@PathVariable("id") Long userId) {
        return service.activateUser(userId);
    }

    @ApiOperation(value = "Freeze user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully frozen", response = UserDTO.class)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/freeze")
    public UserDTO freezeUser(@PathVariable("id") Long userId) {
        return service.freezeUser(userId);
    }

    @ApiOperation(value = "Ban user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully banned", response = UserDTO.class)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/ban")
    public UserDTO banUser(@PathVariable("id") Long userId) {
        return service.banUser(userId);
    }

    @ApiOperation(value = "Replenish the balance")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully replenished", response = UserDTO.class)})
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/balance")
    public UserDTO replenishBalance(@PathVariable("id") Long userId,
                                    @RequestParam("sum") Double sum) {
        return service.replenishBalance(userId, sum);
    }

}
