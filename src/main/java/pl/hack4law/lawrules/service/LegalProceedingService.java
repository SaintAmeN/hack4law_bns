package pl.hack4law.lawrules.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.hack4law.lawrules.model.*;
import pl.hack4law.lawrules.model.dto.AddLegalProceedingRequest;
import pl.hack4law.lawrules.repository.LegalProceedingRepository;
import pl.hack4law.lawrules.repository.ProceedingAvailableStepRepository;
import pl.hack4law.lawrules.repository.ProceedingStepRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static pl.hack4law.lawrules.configuration.BasicConfig.INITIAL_STEPS;

@Slf4j
@Service
@RequiredArgsConstructor
public class LegalProceedingService {

    private final Checkers checkers;
    private final ProceedingStepRepository proceedingStepRepository;
    private final ProceedingAvailableStepRepository proceedingAvailableStepRepository;
    private final LegalProceedingRepository legalProceedingRepository;

    public void add(AddLegalProceedingRequest request, Account owner) {
        LegalProceeding proceeding = LegalProceeding.builder()
                .name(request.getName())
                .claim(request.getClaim())
                .creator(owner)
                .creditor(new ProceedingEntity(null, request.getCreditorName(), request.getCreditorEmail()))
                .debtor(new ProceedingEntity(null, request.getDebtorName(), request.getDebtorEmail()))
                .dateTimeCreated(LocalDateTime.now())
                .proceedingSteps(getAvailableSteps(INITIAL_STEPS))
                .build();

        legalProceedingRepository.save(proceeding);
    }

    private List<ProceedingStep> getAvailableSteps(List<StepName> stepNames) {
        return stepNames.stream()
                .map(name -> {
                    return proceedingAvailableStepRepository.findByName(name).orElse(null);
                }).filter(Objects::nonNull)
                .map(availableStep -> {
                    ProceedingStep step = ProceedingStep.builder()
                            .dateTimeDeadline(availableStep.getDeadlineDuration()!= null ? LocalDateTime.now().plus(availableStep.getDeadlineDuration()): null)
                            .name(availableStep.getName())
                            .build();
                    return proceedingStepRepository.save(step);
                }).collect(Collectors.toList());
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
        List<StepName> stepsCompleted = proceeding.getProceedingSteps().stream()
                .filter(proceedingStep -> proceedingStep.getDateTimeCompleted() == null)
                .filter(proceedingStep -> checkers.checkSucceeded(proceedingStep.getName(), proceeding, proceedingStep))
                .map(proceedingStep -> {
                    proceedingStep.setDateTimeCompleted(LocalDateTime.now());
                    proceedingStepRepository.save(proceedingStep);
                    return proceedingStep.getName();
                })
                .map(name -> proceedingAvailableStepRepository.findByName(name).orElse(null))
                .filter(availableStep -> availableStep != null)
                .flatMap(availableStep -> availableStep.getChangeOnSuccessful().stream())
                .collect(Collectors.toList());

        proceeding.getProceedingSteps().addAll(getAvailableSteps(stepsCompleted));
        legalProceedingRepository.save(proceeding);
    }

    private void checkStepFailed(LegalProceeding proceeding) {
        List<StepName> stepsCompleted = proceeding.getProceedingSteps().stream()
                .filter(proceedingStep -> proceedingStep.getDateTimeCompleted() == null)
                .filter(proceedingStep -> checkers.checkFailed(proceedingStep.getName(), proceeding, proceedingStep))
                .map(proceedingStep -> {
                    proceedingStep.setDateTimeCompleted(LocalDateTime.now());
                    proceedingStepRepository.save(proceedingStep);
                    return proceedingStep.getName();
                })
                .map(name -> proceedingAvailableStepRepository.findByName(name).orElse(null))
                .filter(availableStep -> availableStep != null)
                .flatMap(availableStep -> availableStep.getChangeOnFailed().stream())
                .collect(Collectors.toList());

        proceeding.getProceedingSteps().addAll(getAvailableSteps(stepsCompleted));
        legalProceedingRepository.save(proceeding);
    }

    public void complete(String id, String stepId) {
        LegalProceeding proceeding = getProceeding(id);
        for (ProceedingStep proceedingStep : proceeding.getProceedingSteps()) {
            if (proceedingStep.getId().equals(stepId)){
                proceedingStep.setDateTimeCompleted(LocalDateTime.now());
                proceedingStepRepository.save(proceedingStep);

                proceeding.getProceedingSteps().addAll(getAvailableSteps(proceedingAvailableStepRepository.findByName(proceedingStep.getName()).orElse(null).getChangeOnSuccessful()));
                legalProceedingRepository.save(proceeding);
                return;
            }
        }
    }

//    public List<AnonymizedDocumentDto> getDocuments(boolean shared, Account currentUser){
//
//    }
}
