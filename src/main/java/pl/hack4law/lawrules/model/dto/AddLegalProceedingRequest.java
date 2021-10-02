package pl.hack4law.lawrules.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddLegalProceedingRequest {
    private String name;
    private Double claim;

    private String creditorName;
    private String creditorEmail;
    private String debtorName;
    private String debtorEmail;

}
