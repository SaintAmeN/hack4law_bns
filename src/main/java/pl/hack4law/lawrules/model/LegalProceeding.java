package pl.hack4law.lawrules.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

    private Set<ProceedingStep> proceedingSteps;
    private Set<ProceedingNote> proceedingNotes;

    private ProceedingStep lastStep;
    private boolean markedFailed = false;

    public String getEntitiesNames(){
        return String.format("%s / %s", creditor.getName(), debtor.getName());
    }

    public String getState(){
        if ( markedFailed) {
            return "Completed unsuccessful";
        }
        return isCompleted() ? "Completed" : (lastStep!= null ? lastStep.getName().getContentPL() : "Newly created");
    }

    public boolean isCompleted(){
        return remainingSteps().isEmpty();
    }

    public List<ProceedingStep> remainingSteps(){
        return proceedingSteps.stream()
                .filter(proceedingStep -> !proceedingStep.getName().isEnding())
                .filter(proceedingStep -> proceedingStep.getDateTimeCompleted() == null)
                .collect(Collectors.toList());
    }
}
