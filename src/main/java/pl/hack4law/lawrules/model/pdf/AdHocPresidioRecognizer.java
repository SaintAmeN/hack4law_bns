package pl.hack4law.lawrules.model.pdf;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.hack4law.lawrules.model.DocumentLanguage;

import javax.print.Doc;
import java.util.Arrays;
import java.util.List;

import static pl.hack4law.lawrules.model.pdf.AdHocPresidioRecognizerPattern.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AdHocPresidioRecognizer {
    private String name;
    private String supportedLanguage;
    private List<AdHocPresidioRecognizerPattern> patterns;
    private List<String> context;
    private String supportedEntity;

    public static AdHocPresidioRecognizer createZipCodeRecognizer(){
        return AdHocPresidioRecognizer.builder()
                .name("Zip code Recognizer")
                .supportedLanguage(DocumentLanguage.ENGLISH.getLanguage())
                .patterns(Arrays.asList(
                        createAdHocPresidioRecognizerUSZipCode(),
                        createAdHocPresidioRecognizerPLZipCode()
                ))
                .context(Arrays.asList("zip", "code"))
                .supportedEntity("ZIP")
                .build();
    }

    public static AdHocPresidioRecognizer createStreetNameRecognizer(){
        return AdHocPresidioRecognizer.builder()
                .name("PL street recognizer")
                .supportedLanguage(DocumentLanguage.ENGLISH.getLanguage())
                .patterns(Arrays.asList(
                        createAdHocPresidioRecognizerPLStreetName(),
                        createAdHocPresidioRecognizerPLStreetNameNoNumber()
                ))
                .context(Arrays.asList("street", "ul."))
                .supportedEntity("STREET_NAME")
                .build();
    }

    public static AdHocPresidioRecognizer createCurrencyRecognizer(){
        return AdHocPresidioRecognizer.builder()
                .name("PL currency")
                .supportedLanguage(DocumentLanguage.ENGLISH.getLanguage())
                .patterns(Arrays.asList(
                        createAdHocPresidioRecognizerPolishCurrency()
                ))
                .context(Arrays.asList("street", "ul."))
                .supportedEntity("CURRENCY")
                .build();
    }

    public static AdHocPresidioRecognizer createParagraphRecognizer(){
        return AdHocPresidioRecognizer.builder()
                .name("notes paragraph")
                .supportedLanguage(DocumentLanguage.ENGLISH.getLanguage())
                .patterns(Arrays.asList(
                        createAdHocPresidioRecognizerPolishParagraphArticle(),
                        createAdHocPresidioRecognizerPolishParagraph()
                ))
                .context(Arrays.asList("art.", "paragraph"))
                .supportedEntity("ARTICLES_PARAGRAPHS")
                .build();
    }


}
