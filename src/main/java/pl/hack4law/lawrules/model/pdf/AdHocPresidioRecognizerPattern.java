package pl.hack4law.lawrules.model.pdf;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AdHocPresidioRecognizerPattern {
    private String name;
    private String regex;
    private double score;

    public static AdHocPresidioRecognizerPattern createAdHocPresidioRecognizerUSZipCode() {
        return AdHocPresidioRecognizerPattern.builder()
                .name("zip code (weak)")
                .regex("(\\b\\d{5}(?:\\-\\d{4})?\\b)")
                .score(0.01).build();
    }
    public static AdHocPresidioRecognizerPattern createAdHocPresidioRecognizerPLZipCode() {
        return AdHocPresidioRecognizerPattern.builder()
                .name("kod pocztowy")
                .regex("(\\d\\d-\\d\\d\\d)")
                .score(0.01).build();
    }


    public static AdHocPresidioRecognizerPattern createAdHocPresidioRecognizerPLStreetName() {
        return AdHocPresidioRecognizerPattern.builder()
                .name("adres")
                .regex("(ul\\.*(?:\\-\\d{2})/(?:\\-\\d{2}))")
                .score(0.01).build();
    }

    public static AdHocPresidioRecognizerPattern createAdHocPresidioRecognizerPLStreetNameNoNumber() {
        return AdHocPresidioRecognizerPattern.builder()
                .name("adres")
                .regex("(ul\\.*)")
                .score(0.01).build();
    }

    public static AdHocPresidioRecognizerPattern createAdHocPresidioRecognizerPolishCurrency() {
        return AdHocPresidioRecognizerPattern.builder()
                .name("kwoty zł")
                .regex("((\\d{1,3} )?\\d{1,3},\\d\\d zł)")
                .score(0.01).build();
    }

    public static AdHocPresidioRecognizerPattern createAdHocPresidioRecognizerPolishParagraph() {
        return AdHocPresidioRecognizerPattern.builder()
                .name("paragraf 824 § 1 pkt 3")
                .regex("((\\d{1,3})? § (\\d{1,3})? pkt (\\d{1,3})?)")
                .score(0.01).build();
    }

    public static AdHocPresidioRecognizerPattern createAdHocPresidioRecognizerPolishParagraphArticle() {
        return AdHocPresidioRecognizerPattern.builder()
                .name("art. 826")
                .regex("(art. (\\d{1,4})? (§)?)")
                .score(0.01).build();
    }
}
