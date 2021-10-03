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
        if (!proceedingAvailableStepRepository.findByName(BAILIFF_ADVANCE_COSTS_ESTIMATION).isPresent()) {
            proceedingAvailableStepRepository.save(Create_BAILIFF_ADVANCE_COSTS_ESTIMATION());
        }
        if (!proceedingAvailableStepRepository.findByName(BAILIFF_ADVANCE_COSTS_SENT_TO_CREDITOR).isPresent()) {
            proceedingAvailableStepRepository.save(Create_BAILIFF_ADVANCE_COSTS_SENT_TO_CREDITOR());
        }
        if (!proceedingAvailableStepRepository.findByName(BAILIFF_ADVANCE_COSTS_RECEIVED_BY_CREDITOR).isPresent()) {
            proceedingAvailableStepRepository.save(Create_BAILIFF_ADVANCE_COSTS_RECEIVED_BY_CREDITOR());
        }
        if (!proceedingAvailableStepRepository.findByName(CREDITOR_PAID_ADVANCE).isPresent()) {
            proceedingAvailableStepRepository.save(Create_CREDITOR_PAID_ADVANCE());
        }
        if (!proceedingAvailableStepRepository.findByName(CHECK_THE_GIVEN_ACCOUNT).isPresent()) {
            proceedingAvailableStepRepository.save(Create_CHECK_THE_GIVEN_ACCOUNT());
        }
        if (!proceedingAvailableStepRepository.findByName(CHECK_BANKS_FOR_ACCOUNT).isPresent()) {
            proceedingAvailableStepRepository.save(Create_CASE_CHECK_BANKS_FOR_ACCOUNT());
        }
        if (!proceedingAvailableStepRepository.findByName(SECURE_THE_DEBTS_ON_DEBTORS_ACCOUNT).isPresent()) {
            proceedingAvailableStepRepository.save(Create_CASE_SECURE_THE_DEBTS_ON_DEBTORS_ACCOUNT());
        }
        if (!proceedingAvailableStepRepository.findByName(SEND_NOTIFICATION_TO_CREDITOR).isPresent()) {
            proceedingAvailableStepRepository.save(Create_CASE_SEND_NOTIFICATION_TO_CREDITOR());
        }
        if (!proceedingAvailableStepRepository.findByName(GET_THE_CLAIM_FROM_DEBTORS_ACCOUNT).isPresent()) {
            proceedingAvailableStepRepository.save(Create_CASE_GET_THE_CLAIM_FROM_DEBTORS_ACCOUNT());
        }
        if (!proceedingAvailableStepRepository.findByName(SUMMARIZE_CAUSE_COST).isPresent()) {
            proceedingAvailableStepRepository.save(Create_CASE_SUMMARIZE_CAUSE_COST());
        }
        if (!proceedingAvailableStepRepository.findByName(SEND_DEBT_TO_CREDITOR).isPresent()) {
            proceedingAvailableStepRepository.save(Create_CASE_SEND_DEBT_TO_CREDITOR());
        }
        if (!proceedingAvailableStepRepository.findByName(CASE_CLOSED).isPresent()) {
            proceedingAvailableStepRepository.save(Create_CASE_CLOSED());
        }
        if (!proceedingAvailableStepRepository.findByName(CASE_DISCOUNTED_BY_CREDITOR).isPresent()) {
            proceedingAvailableStepRepository.save(Create_CASE_DISCOUNTED_BY_CREDITOR());
        }
        if (!proceedingAvailableStepRepository.findByName(CHECK_CEPIK_AND_ZUS).isPresent()) {
            proceedingAvailableStepRepository.save(Create_CASE_CHECK_CEPIK_AND_ZUS());
        }
        if (!proceedingAvailableStepRepository.findByName(SEIZURE_OF_MOVABLE_PROPERTY).isPresent()) {
            proceedingAvailableStepRepository.save(Create_CASE_SEIZURE_OF_MOVABLE_PROPERTY());
        }
        if (!proceedingAvailableStepRepository.findByName(REQUEST_LIST_OF_ASSETS_TO_DEBTOR).isPresent()) {
            proceedingAvailableStepRepository.save(Create_CASE_REQUEST_LIST_OF_ASSETS_TO_DEBTOR());
        }
        if (!proceedingAvailableStepRepository.findByName(AUCTION).isPresent()) {
            proceedingAvailableStepRepository.save(Create_CASE_AUCTION());
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
                .changeOnFailed(Arrays.asList())
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
                .changeOnSuccessful(Arrays.asList(CHECK_THE_GIVEN_ACCOUNT, CASE_DISCOUNTED_BY_CREDITOR))
                .changeOnFailed(Arrays.asList())
                .build();
    }

    private AvailableStep Create_CHECK_THE_GIVEN_ACCOUNT() {
        return AvailableStep.builder()
                .name(CHECK_THE_GIVEN_ACCOUNT)
                .deadlineDuration(Duration.of(3, DAYS))
                .changeOnSuccessful(Arrays.asList(SECURE_THE_DEBTS_ON_DEBTORS_ACCOUNT, CASE_DISCOUNTED_BY_CREDITOR))
                .changeOnFailed(Arrays.asList(CHECK_BANKS_FOR_ACCOUNT))
                .build();
    }

    //    private AvailableStep Create_CASE_CHECK_BANK_FROM_APPLICATION() {
//        return AvailableStep.builder()
//                .name(CHECK_BANK_FROM_APPLICATION)
//                .toComplete(null)
//                .deadlineDuration(Duration.of(3, DAYS))
//                .changeOnSuccessful(Arrays.asList(CHECK_BANKS_FOR_ACCOUNT, CASE_DISCOUNTED_BY_CREDITOR))
////                .changeOnFailed(Arrays.asList(CREDITOR_FAILED_TO_PAY))
//                .build();
//    }
    private AvailableStep Create_CASE_CHECK_BANKS_FOR_ACCOUNT() {
        return AvailableStep.builder()
                .name(CHECK_BANKS_FOR_ACCOUNT)
                .deadlineDuration(Duration.of(3, DAYS))
                .changeOnSuccessful(Arrays.asList(CHECK_BANKS_FOR_ACCOUNT, CASE_DISCOUNTED_BY_CREDITOR))
                .changeOnFailed(Arrays.asList(CHECK_CEPIK_AND_ZUS))
                .build();
    }

    private AvailableStep Create_CASE_CHECK_CEPIK_AND_ZUS() {
        return AvailableStep.builder()
                .name(CHECK_CEPIK_AND_ZUS)
                .deadlineDuration(Duration.of(3, DAYS))
                .changeOnSuccessful(Arrays.asList(SEIZURE_OF_MOVABLE_PROPERTY, CASE_DISCOUNTED_BY_CREDITOR))
                .changeOnFailed(Arrays.asList(REQUEST_LIST_OF_ASSETS_TO_DEBTOR))
                .build();
    }

    private AvailableStep Create_CASE_SEIZURE_OF_MOVABLE_PROPERTY() {
        return AvailableStep.builder()
                .name(SEIZURE_OF_MOVABLE_PROPERTY)
                .deadlineDuration(Duration.of(30, DAYS))
                .changeOnSuccessful(Arrays.asList(AUCTION, CASE_DISCOUNTED_BY_CREDITOR))
                .changeOnFailed(Arrays.asList())
                .build();
    }

    private AvailableStep Create_CASE_REQUEST_LIST_OF_ASSETS_TO_DEBTOR() {
        return AvailableStep.builder()
                .name(REQUEST_LIST_OF_ASSETS_TO_DEBTOR)
                .deadlineDuration(Duration.of(14, DAYS))
                .changeOnSuccessful(Arrays.asList(AUCTION, CASE_DISCOUNTED_BY_CREDITOR))
                .changeOnFailed(Arrays.asList())
                .build();
    }

    private AvailableStep Create_CASE_AUCTION() {
        return AvailableStep.builder()
                .name(AUCTION)
                .deadlineDuration(Duration.of(14, DAYS))
                .changeOnSuccessful(Arrays.asList(GET_THE_CLAIM_FROM_DEBTORS_ACCOUNT, CASE_DISCOUNTED_BY_CREDITOR))
                .changeOnFailed(Arrays.asList())
                .build();
    }

    private AvailableStep Create_CASE_SECURE_THE_DEBTS_ON_DEBTORS_ACCOUNT() {
        return AvailableStep.builder()
                .name(SECURE_THE_DEBTS_ON_DEBTORS_ACCOUNT)
                .deadlineDuration(Duration.of(3, DAYS))
                .changeOnSuccessful(Arrays.asList(SEND_NOTIFICATION_TO_CREDITOR, CASE_DISCOUNTED_BY_CREDITOR))
                .changeOnFailed(Arrays.asList(CHECK_BANKS_FOR_ACCOUNT))
                .build();
    }


    // krok niżej powinien pozwolić na następny krok nie wcześniej niż po tygodniu
    private AvailableStep Create_CASE_SEND_NOTIFICATION_TO_CREDITOR() {
        return AvailableStep.builder()
                .name(SEND_NOTIFICATION_TO_CREDITOR)
                .deadlineDuration(Duration.of(14, DAYS))
                .changeOnSuccessful(Arrays.asList(GET_THE_CLAIM_FROM_DEBTORS_ACCOUNT, CASE_DISCOUNTED_BY_CREDITOR))
                .changeOnFailed(Arrays.asList())
//                .changeOnFailed(Arrays.asList(CREDITOR_FAILED_TO_PAY))
                .build();
    }

    private AvailableStep Create_CASE_GET_THE_CLAIM_FROM_DEBTORS_ACCOUNT() {
        return AvailableStep.builder()
                .name(GET_THE_CLAIM_FROM_DEBTORS_ACCOUNT)
                .deadlineDuration(Duration.of(7, DAYS))
                .changeOnSuccessful(Arrays.asList(CHECK_BANKS_FOR_ACCOUNT, CASE_DISCOUNTED_BY_CREDITOR))
                .changeOnFailed(Arrays.asList())
//                .changeOnFailed(Arrays.asList(CREDITOR_FAILED_TO_PAY))
                .build();
    }

    private AvailableStep Create_CASE_SUMMARIZE_CAUSE_COST() {
        return AvailableStep.builder()
                .name(SUMMARIZE_CAUSE_COST)
                .deadlineDuration(Duration.of(3, DAYS))
                .changeOnSuccessful(Arrays.asList(SEND_DEBT_TO_CREDITOR, CASE_DISCOUNTED_BY_CREDITOR))
                .changeOnFailed(Arrays.asList())
//                .changeOnFailed(Arrays.asList(CREDITOR_FAILED_TO_PAY))
                .build();
    }

    private AvailableStep Create_CASE_SEND_DEBT_TO_CREDITOR() {
        return AvailableStep.builder()
                .name(SEND_DEBT_TO_CREDITOR)
                .deadlineDuration(Duration.of(3, DAYS))
                .changeOnSuccessful(Arrays.asList(CASE_CLOSED))
                .changeOnFailed(Arrays.asList())
//                .changeOnFailed(Arrays.asList(CREDITOR_FAILED_TO_PAY))
                .build();
    }

    private AvailableStep Create_CASE_CLOSED() {
        return AvailableStep.builder()
                .name(CASE_CLOSED)
                .deadlineDuration(Duration.of(3, DAYS))
//                               .changeOnSuccessful(Arrays.asList(CASE_CLOSED))
                .changeOnSuccessful(Arrays.asList())
                .changeOnFailed(Arrays.asList())
//                .changeOnFailed(Arrays.asList(CREDITOR_FAILED_TO_PAY))
                .build();
    }

    private AvailableStep Create_CASE_DISCOUNTED_BY_CREDITOR() {
        return AvailableStep.builder()
                .name(CASE_DISCOUNTED_BY_CREDITOR)
                .deadlineDuration(Duration.of(0, DAYS))
                .changeOnSuccessful(Arrays.asList())
                .changeOnFailed(Arrays.asList())
//                .changeOnSuccessful(Arrays.asList(CREDITOR_PAID_ADVANCE))
//                .changeOnFailed(Arrays.asList(CREDITOR_FAILED_TO_PAY))
                .build();
    }
}
