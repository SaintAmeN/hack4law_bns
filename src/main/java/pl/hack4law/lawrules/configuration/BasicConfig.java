package pl.hack4law.lawrules.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import pl.hack4law.lawrules.model.StepName;

import java.util.Arrays;
import java.util.List;

@Configuration
public class BasicConfig {

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    public final static List<StepName> INITIAL_STEPS = Arrays.asList(StepName.BAILIFF_ADVANCE_COSTS_ESTIMATION);
}
