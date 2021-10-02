package pl.hack4law.lawrules.service.pdf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.hack4law.lawrules.configuration.PresidioConfig;
import pl.hack4law.lawrules.model.pdf.PresidioAnalyzerRequest;
import pl.hack4law.lawrules.model.pdf.PresidioAnalyzerResponse;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyzerClient {
    private final PresidioConfig config;
    private final RestTemplate restTemplate;

    public List<PresidioAnalyzerResponse> sendAnalysisRequest(PresidioAnalyzerRequest request){
        HttpEntity<PresidioAnalyzerRequest> requestHttpEntity = new HttpEntity<>(request);
        ResponseEntity<List<PresidioAnalyzerResponse>> response = restTemplate.exchange(config.getAnalyzer()+"/analyze", HttpMethod.POST, requestHttpEntity, new ParameterizedTypeReference<List<PresidioAnalyzerResponse>>() {});
        log.info(String.format("Result is: %s", response));

        if(response.getStatusCode().is2xxSuccessful()){
            log.info("Result is ok.");
            return response.getBody();
        }
        log.info("Result is failed.");
        return new ArrayList<>();
    }
}
