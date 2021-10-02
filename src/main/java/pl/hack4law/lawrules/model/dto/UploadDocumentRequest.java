package pl.hack4law.lawrules.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import pl.hack4law.lawrules.model.DocumentLanguage;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadDocumentRequest {
    private MultipartFile document;
    private String documentName;
    private DocumentLanguage language;
    private Boolean allowShared;

    public boolean sharingAllowed() {
        return allowShared != null && allowShared;
//                (allowShared.equalsIgnoreCase("on") ||
//                        allowShared.equalsIgnoreCase("yes") ||
//                        allowShared.equalsIgnoreCase("true"));
    }
}
