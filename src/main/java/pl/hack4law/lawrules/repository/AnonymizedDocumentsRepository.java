package pl.hack4law.lawrules.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.hack4law.lawrules.model.Account;
import pl.hack4law.lawrules.model.AccountRole;
import pl.hack4law.lawrules.model.AnonymizedDocument;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnonymizedDocumentsRepository extends MongoRepository<AnonymizedDocument, String> {
    List<AnonymizedDocument> findAllByOwnerOrShared(Account owner, boolean shared);

}
