package pl.hack4law.lawrules.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.hack4law.lawrules.model.DocumentLanguage;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnonymizedDocumentDto {
    private String id;
    private String documentName;
    private LocalDateTime dateAdded;
    private String documentFilename;
    private DocumentLanguage language;
    private boolean sharingAllowed;
    private String ownerUsername;

}
