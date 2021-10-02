package pl.hack4law.lawrules.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.hack4law.lawrules.model.AccountRole;

import java.util.Optional;

@Repository
public interface AccountRoleRepository extends MongoRepository<AccountRole, String> {

    Optional<AccountRole> findByName(String name);
    boolean existsByName(String name);
}
