package daniyar.idrisov.test.storeservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationWithChildsDTO {

    private Long id;
    private String header;

    private String text;

    private UserDTO user;

    private UserDTO createdBy;

}
