package pl.hack4law.lawrules.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.hack4law.lawrules.model.*;
import pl.hack4law.lawrules.model.dto.AddLegalProceedingRequest;
import pl.hack4law.lawrules.repository.LegalProceedingRepository;
import pl.hack4law.lawrules.repository.ProceedingAvailableStepRepository;
import pl.hack4law.lawrules.repository.ProceedingNoteRepository;
import pl.hack4law.lawrules.repository.ProceedingStepRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static pl.hack4law.lawrules.configuration.BasicConfig.INITIAL_STEPS;

@Slf4j
@Service
@RequiredArgsConstructor
public class LegalProceedingService {

    private final Checkers checkers;
    private final ProceedingStepRepository proceedingStepRepository;
    private final ProceedingNoteRepository proceedingNoteRepository;
    private final ProceedingAvailableStepRepository proceedingAvailableStepRepository;
    private final LegalProceedingRepository legalProceedingRepository;

    public void add(AddLegalProceedingRequest request, Account owner) {
        LegalProceeding proceeding = LegalProceeding.builder()
                .name(request.getName())
                .claim(request.getClaim())
                .creator(owner)
                .creditor(new ProceedingEntity(null, request.getCreditorName(), request.getCreditorEmail(), request.getCreditorAccountNumber()))
                .debtor(new ProceedingEntity(null, request.getDebtorName(), request.getDebtorEmail(), request.getDebtorAccountNumber()))
                .dateTimeCreated(LocalDateTime.now())
                .proceedingSteps(appendStepsWithNames(INITIAL_STEPS))
                .build();

        legalProceedingRepository.save(proceeding);
    }

    private Set<ProceedingStep> appendStepsWithNames(List<StepName> stepNames) {
        return stepNames.stream()
                .map(proceedingAvailableStepRepository::findByName)
                .filter(Optional::isPresent)
                .map(availableStep -> createStepFromAvailableSteps(availableStep.get()))
                .collect(Collectors.toSet());
    }

    private ProceedingStep createStepFromAvailableSteps(AvailableStep availableStep) {
        return proceedingStepRepository.save(ProceedingStep.builder()
                .changeOnFailed(new ArrayList<>(availableStep.getChangeOnFailed()))
                .changeOnSuccessful(new ArrayList<>(availableStep.getChangeOnSuccessful()))
                .dateTimeDeadline(availableStep.getDeadlineDuration() != null ? LocalDateTime.now().plus(availableStep.getDeadlineDuration()) : null)
                .name(availableStep.getName())
                .build());
    }

    public List<LegalProceeding> getProceedings(Account accountFromPrincipal) {
        return legalProceedingRepository.findByCreator(accountFromPrincipal);
    }

    public LegalProceeding getProceeding(String id) {
        return legalProceedingRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void setAdvance(String legalproceedingid, double advance) {
        LegalProceeding proceeding = getProceeding(legalproceedingid);
        proceeding.setFees(advance);
        legalProceedingRepository.save(proceeding);

        checkStepSuccess(proceeding);
        checkStepFailed(proceeding);
    }

    private void checkStepSuccess(LegalProceeding proceeding) {
        proceeding.remainingSteps().stream()
                .filter(proceedingStep -> {
                    if (checkers.checkSucceeded(proceedingStep.getName(), proceeding, proceedingStep)) {
                        proceedingStep.setDateTimeCompleted(LocalDateTime.now());
                        proceedingStepRepository.save(proceedingStep);

                        proceeding.setLastStep(proceedingStep);
                        legalProceedingRepository.save(proceeding);
                        return true;
                    }
                    return false;
                })
                .flatMap(proceedingStep -> proceedingStep.getChangeOnSuccessful().stream())
                .map(stepName -> proceedingAvailableStepRepository.findByName(stepName))
                .filter(availableStep -> availableStep.isPresent())
                .map(availableStep -> createStepFromAvailableSteps(availableStep.get()))
                .forEach(proceeding.getProceedingSteps()::add);

        legalProceedingRepository.save(proceeding);
    }

    private void checkStepFailed(LegalProceeding proceeding) {
        proceeding.remainingSteps().stream()
                .filter(proceedingStep -> {
                    if (checkers.checkFailed(proceedingStep.getName(), proceeding, proceedingStep)) {
                        proceedingStep.setDateTimeCompleted(LocalDateTime.now());
                        proceedingStepRepository.save(proceedingStep);

                        proceeding.setLastStep(proceedingStep);
                        legalProceedingRepository.save(proceeding);
                        return true;
                    }
                    return false;
                })
                .flatMap(proceedingStep -> proceedingStep.getChangeOnFailed().stream())
                .map(stepName -> proceedingAvailableStepRepository.findByName(stepName))
                .filter(availableStep -> availableStep.isPresent())
                .map(availableStep -> createStepFromAvailableSteps(availableStep.get()))
                .forEach(proceeding.getProceedingSteps()::add);

        legalProceedingRepository.save(proceeding);
    }

    public void complete(String id, String stepId) throws UnsupportedOperationException {
        LegalProceeding proceeding = getProceeding(id);
        for (ProceedingStep proceedingStep : proceeding.getProceedingSteps()) {
            if (proceedingStep.getId().equals(stepId) && checkers.canBeCompleted(proceedingStep.getName(), proceeding, proceedingStep)) {
                proceedingStep.setDateTimeCompleted(LocalDateTime.now());
                proceedingStepRepository.save(proceedingStep);
                proceeding.setLastStep(proceedingStep);

                proceeding.getProceedingSteps().addAll(appendStepsWithNames(proceedingAvailableStepRepository.findByName(proceedingStep.getName()).orElse(null).getChangeOnSuccessful()));
                legalProceedingRepository.save(proceeding);
            }
            if (!checkers.canBeCompleted(proceedingStep.getName(), proceeding, proceedingStep)) {
                throw new UnsupportedOperationException("Task can't be closed Yet.");
            }
        }
    }

    public void addNote(String proceedingId, String note, Account accountFromPrincipal) {
        LegalProceeding proceeding = getProceeding(proceedingId);
        proceeding.getProceedingNotes().add(proceedingNoteRepository.save(new ProceedingNote(null, LocalDateTime.now(), note, accountFromPrincipal)));

        legalProceedingRepository.save(proceeding);
    }
}
