package pl.hack4law.lawrules.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class ProceedingNote {

    @Id
    private String id;

    private LocalDateTime dateTimeCreated;
    private String note;
    private Account user;
}
