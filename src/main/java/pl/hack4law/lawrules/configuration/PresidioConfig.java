package pl.hack4law.lawrules.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("presidio.baseurl")
public class PresidioConfig {
    private String analyzer;
    private String anonymizer;
    private String imageRedactor;
}
