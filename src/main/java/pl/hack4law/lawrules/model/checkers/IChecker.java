package pl.hack4law.lawrules.model.checkers;

import pl.hack4law.lawrules.model.LegalProceeding;
import pl.hack4law.lawrules.model.ProceedingStep;

public interface IChecker {
    boolean checkSucceeded(LegalProceeding proceeding, ProceedingStep step);
    boolean checkFailed(LegalProceeding proceeding, ProceedingStep step);
}
