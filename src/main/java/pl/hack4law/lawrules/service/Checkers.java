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

    public boolean canBeCompleted(StepName name, LegalProceeding proceeding, ProceedingStep proceedingStep) {
        return checkerMap.get(name).canBeCompleted(proceeding, proceedingStep);
    }

    public boolean checkSucceeded(StepName name, LegalProceeding proceeding, ProceedingStep proceedingStep) {
        if (checkerMap.get(name).canBeCompleted(proceeding, proceedingStep)) {
            return checkerMap.get(name).checkSucceeded(proceeding, proceedingStep);
        }
        return false;
    }

    public boolean checkFailed(StepName name, LegalProceeding proceeding, ProceedingStep proceedingStep) {
        if (checkerMap.get(name).canBeCompleted(proceeding, proceedingStep)) {
            return checkerMap.get(name).checkFailed(proceeding, proceedingStep);
        }
        return false;
    }

    public Checkers() {
        for (StepName value : values()) {
            checkerMap.put(value, new IChecker() {});
        }

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

        checkerMap.put(SEND_NOTIFICATION_TO_CREDITOR, new IChecker() {
            @Override
            public boolean canBeCompleted(LegalProceeding proceeding, ProceedingStep step) {
                return LocalDateTime.now().isAfter(step.getDateTimeCompleted().plusDays(7));
            }
        });
    }
}
