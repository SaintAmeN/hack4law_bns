package pl.hack4law.lawrules.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.hack4law.lawrules.model.LegalProceeding;
import pl.hack4law.lawrules.model.ProceedingStep;
import pl.hack4law.lawrules.model.StepName;
import pl.hack4law.lawrules.model.checkers.IChecker;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static pl.hack4law.lawrules.model.StepName.*;

@Slf4j
@Service
public class Checkers {
    private final Map<StepName, IChecker> checkerMap = new HashMap<>();

    public boolean checkSucceeded(StepName name, LegalProceeding proceeding, ProceedingStep proceedingStep) {
        return checkerMap.get(name).checkSucceeded(proceeding, proceedingStep);
    }

    public boolean checkFailed(StepName name, LegalProceeding proceeding, ProceedingStep step) {
        return checkerMap.get(name).checkFailed(proceeding, step);
    }

    public Checkers() {
        checkerMap.put(BAILIFF_ADVANCE_COSTS_ESTIMATION, new IChecker() {
            @Override
            public boolean checkSucceeded(LegalProceeding proceeding, ProceedingStep step) {
                return proceeding.getFees() != null;
            }

            @Override
            public boolean checkFailed(LegalProceeding proceeding, ProceedingStep step) {
                return false;
            }
        });
        checkerMap.put(BAILIFF_ADVANCE_COSTS_SENT_TO_CREDITOR, new IChecker() {
            @Override
            public boolean checkSucceeded(LegalProceeding proceeding, ProceedingStep step) {
                return proceeding.getProceedingSteps().contains(BAILIFF_ADVANCE_COSTS_RECEIVED_BY_CREDITOR);
            }

            @Override
            public boolean checkFailed(LegalProceeding proceeding, ProceedingStep step) {
                return step.getDateTimeDeadline() != null && LocalDateTime.now().isAfter(step.getDateTimeDeadline());
            }
        });
        checkerMap.put(BAILIFF_ADVANCE_COSTS_RECEIVED_BY_CREDITOR, new IChecker() {
            @Override
            public boolean checkSucceeded(LegalProceeding proceeding, ProceedingStep step) {
                return proceeding.getProceedingSteps().contains(CREDITOR_PAID_ADVANCE);
            }

            @Override
            public boolean checkFailed(LegalProceeding proceeding, ProceedingStep step) {
                return step.getDateTimeDeadline() != null && LocalDateTime.now().isAfter(step.getDateTimeDeadline());
            }
        });
    }
}
