package pl.hack4law.lawrules.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.hack4law.lawrules.model.checkers.IChecker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class ProceedingStep {

    @Id
    private String id;

    private StepName name;
    private LocalDateTime createdDate;
    private LocalDateTime dateTimeCompleted;
    private LocalDateTime dateTimeDeadline;

    private String note;

    private List<StepName> changeOnSuccessful;
    private List<StepName> changeOnFailed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProceedingStep that = (ProceedingStep) o;
        return name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
