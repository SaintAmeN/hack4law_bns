package pl.hack4law.lawrules.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import pl.hack4law.lawrules.model.*;
import pl.hack4law.lawrules.repository.ProceedingAvailableStepRepository;

import java.time.Duration;
import java.util.Arrays;

import static java.time.temporal.ChronoUnit.DAYS;
import static pl.hack4law.lawrules.model.StepName.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProceedingStepsInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final ProceedingAvailableStepRepository proceedingAvailableStepRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (!proceedingAvailableStepRepository.findByName(BAILIFF_ADVANCE_COSTS_ESTIMATION).isPresent()){
            proceedingAvailableStepRepository.save(Create_BAILIFF_ADVANCE_COSTS_ESTIMATION());
        }
        if (!proceedingAvailableStepRepository.findByName(BAILIFF_ADVANCE_COSTS_SENT_TO_CREDITOR).isPresent()){
            proceedingAvailableStepRepository.save(Create_BAILIFF_ADVANCE_COSTS_SENT_TO_CREDITOR());
        }
        if (!proceedingAvailableStepRepository.findByName(BAILIFF_ADVANCE_COSTS_RECEIVED_BY_CREDITOR).isPresent()){
            proceedingAvailableStepRepository.save(Create_BAILIFF_ADVANCE_COSTS_RECEIVED_BY_CREDITOR());
        }
        if (!proceedingAvailableStepRepository.findByName(CREDITOR_PAID_ADVANCE).isPresent()){
            proceedingAvailableStepRepository.save(Create_CREDITOR_PAID_ADVANCE());
        }
    }

    private AvailableStep Create_BAILIFF_ADVANCE_COSTS_ESTIMATION() {
        return AvailableStep.builder()
                .name(BAILIFF_ADVANCE_COSTS_ESTIMATION)
                .deadlineDuration(Duration.of(3, DAYS))
                .changeOnSuccessful(Arrays.asList(BAILIFF_ADVANCE_COSTS_SENT_TO_CREDITOR))
                .changeOnFailed(Arrays.asList(DROPPED_NO_ACTION))
                .build();
    }

    private AvailableStep Create_BAILIFF_ADVANCE_COSTS_SENT_TO_CREDITOR() {
        return AvailableStep.builder()
                .name(BAILIFF_ADVANCE_COSTS_SENT_TO_CREDITOR)
                .deadlineDuration(null)
                .changeOnSuccessful(Arrays.asList(BAILIFF_ADVANCE_COSTS_RECEIVED_BY_CREDITOR))
                .build();
    }

    private AvailableStep Create_BAILIFF_ADVANCE_COSTS_RECEIVED_BY_CREDITOR() {
        return AvailableStep.builder()
                .name(BAILIFF_ADVANCE_COSTS_RECEIVED_BY_CREDITOR)
                .deadlineDuration(Duration.of(14, DAYS))
                .changeOnSuccessful(Arrays.asList(CREDITOR_PAID_ADVANCE))
                .changeOnFailed(Arrays.asList(CREDITOR_FAILED_TO_PAY))
                .build();
    }

    private AvailableStep Create_CREDITOR_PAID_ADVANCE() {
        return AvailableStep.builder()
                .name(CREDITOR_PAID_ADVANCE)
                .deadlineDuration(Duration.of(7, DAYS))
//                .changeOnSuccessful(Arrays.asList(CREDITOR_PAID_ADVANCE))
//                .changeOnFailed(Arrays.asList(CREDITOR_FAILED_TO_PAY))
                .build();
    }
}
