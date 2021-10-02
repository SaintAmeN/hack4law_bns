package pl.hack4law.lawrules.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.hack4law.lawrules.model.checkers.IChecker;

import java.time.Duration;
import java.util.List;

@Data
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class AvailableStep {

    @Id
    private String id;

    private StepName name;
    private StepName toComplete;
    private Duration deadlineDuration;

    private List<StepName> changeOnSuccessful;
    private List<StepName> changeOnFailed;
}
