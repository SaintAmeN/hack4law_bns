package pl.hack4law.lawrules.model.checkers;

import pl.hack4law.lawrules.model.LegalProceeding;
import pl.hack4law.lawrules.model.ProceedingStep;
import pl.hack4law.lawrules.model.StepName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public interface IChecker {
    default boolean checkSucceeded(LegalProceeding proceeding, ProceedingStep step){
        List<StepName> allCompletedSteps = proceeding.getProceedingSteps().stream().map(ProceedingStep::getName).collect(Collectors.toList());
        return allCompletedSteps.containsAll(step.getChangeOnSuccessful());
    }

    default boolean checkFailed(LegalProceeding proceeding, ProceedingStep step){
<<<<<<< HEAD
        return step.getDateTimeDeadline() != null && LocalDateTime.now().isAfter(step.getDateTimeDeadline());
=======
        if (step.getDateTimeDeadline() != null && LocalDateTime.now().isAfter(step.getDateTimeDeadline())){
            proceeding.setMarkedFailed(true);
            return true;
        }
        return false;
>>>>>>> 87d9cb2c2503b5ffa2cb181459c2404ccec6af72
    }

    default boolean canBeCompleted(LegalProceeding proceeding, ProceedingStep step){
        return true;
    }
}
