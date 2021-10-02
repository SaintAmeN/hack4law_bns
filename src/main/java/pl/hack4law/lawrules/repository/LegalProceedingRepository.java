package pl.hack4law.lawrules.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.hack4law.lawrules.model.Account;
import pl.hack4law.lawrules.model.LegalProceeding;
import pl.hack4law.lawrules.model.ProceedingStep;
import pl.hack4law.lawrules.model.StepName;

import java.util.List;
import java.util.Optional;

public interface LegalProceedingRepository extends MongoRepository<LegalProceeding, String> {
    List<LegalProceeding> findByCreator(Account account);
}
