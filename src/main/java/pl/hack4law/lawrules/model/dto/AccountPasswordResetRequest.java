package pl.hack4law.lawrules.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountPasswordResetRequest {
    private String accountId;
    @NotEmpty
    @Size(min = 4, max = 72)
    private String resetpassword;


}
