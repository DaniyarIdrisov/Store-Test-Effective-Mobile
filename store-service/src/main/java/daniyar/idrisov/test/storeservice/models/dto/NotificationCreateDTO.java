package daniyar.idrisov.test.storeservice.models.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NotificationCreateDTO {

    @NotBlank
    private String header;

    private String text;


}
