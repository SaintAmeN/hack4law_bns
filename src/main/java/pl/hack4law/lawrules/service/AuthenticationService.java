package pl.hack4law.lawrules.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.hack4law.lawrules.model.Account;
import pl.hack4law.lawrules.model.AccountRole;
import pl.hack4law.lawrules.repository.AccountRepository;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(String.format("Login request: %s", username));
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        if (optionalAccount.isPresent()) {
            log.info("Login successful!");
            return optionalAccount.get();
        }

        throw new UsernameNotFoundException("Username not found.");
    }
}
