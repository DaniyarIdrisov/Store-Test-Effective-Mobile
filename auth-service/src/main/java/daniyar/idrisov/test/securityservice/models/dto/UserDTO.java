package daniyar.idrisov.test.securityservice.models.dto;

import daniyar.idrisov.test.securityservice.models.enumerated.Role;
import daniyar.idrisov.test.securityservice.models.enumerated.UserState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private Double balance;

    private Role role;

    private UserState state;

}
