package pl.hack4law.lawrules.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.hack4law.lawrules.model.ProceedingNote;
import pl.hack4law.lawrules.model.ProceedingStep;
import pl.hack4law.lawrules.model.StepName;

import java.util.Optional;

public interface ProceedingNoteRepository extends MongoRepository<ProceedingNote, String> {
}
