package pl.hack4law.lawrules.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "anonymized_documents")
@AllArgsConstructor
@NoArgsConstructor
public class AnonymizedDocument {
    @Id
    private String id;

    private String name;

    private LocalDateTime dateAdded;

    private String fileName;
    private Binary file;
    private Binary anonymized;

    private boolean shared;
    private Account owner;

    private DocumentLanguage language;
}
