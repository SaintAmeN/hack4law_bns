package pl.hack4law.lawrules.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.hack4law.lawrules.model.checkers.IChecker;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class ProceedingStep {

    @Id
    private String id;

    private StepName name;
    private LocalDateTime dateTimeCompleted;
    private LocalDateTime dateTimeDeadline;

    private String note;
}
