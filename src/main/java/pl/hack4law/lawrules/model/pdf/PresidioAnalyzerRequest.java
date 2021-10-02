package pl.hack4law.lawrules.model.pdf;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PresidioAnalyzerRequest {
    private String text;
    private String language;
    private List<AdHocPresidioRecognizer> adHocRecognizers;
}
