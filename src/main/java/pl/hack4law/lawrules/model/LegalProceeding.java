package pl.hack4law.lawrules.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
// creditor wierzyciel
// debtor dłużnik

@Data
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class LegalProceeding {
    @Id
    private String id;

    private String name;

    // Money
    private Double claim;
    private Double fees;

    private ProceedingEntity creditor;
    private ProceedingEntity debtor;

    private Account creator;
    private LocalDateTime dateTimeCreated;

    private List<ProceedingStep> proceedingSteps;
    private List<ProceedingAction> proceedingActions;

    public String getEntitiesNames(){
        return String.format("%s / %s", creditor.getName(), debtor.getName());
    }

    public String getState(){
        return "TODO";
    }
}
