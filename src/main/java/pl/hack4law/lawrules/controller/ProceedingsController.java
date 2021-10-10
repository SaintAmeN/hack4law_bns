package pl.hack4law.lawrules.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.hack4law.lawrules.model.DocumentLanguage;
import pl.hack4law.lawrules.model.LegalProceeding;
import pl.hack4law.lawrules.model.dto.AddLegalProceedingRequest;
import pl.hack4law.lawrules.model.dto.UploadDocumentRequest;
import pl.hack4law.lawrules.service.AccountService;
import pl.hack4law.lawrules.service.AnonymizedDocumentService;
import pl.hack4law.lawrules.service.LegalProceedingService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;

@Slf4j
@Controller
@RequestMapping(path = "/legalproceedings")
@AllArgsConstructor
public class ProceedingsController {
    private final LegalProceedingService legalProceedingService;
    private final AccountService accountService;

    @GetMapping
    public String proceedings(Model model,
                              Principal principal) {
        model.addAttribute("proceedings", legalProceedingService.getProceedings(accountService.getAccountFromPrincipal(principal)));
        return "proceedings-list";
    }

    @GetMapping("/add")
    public String getLegalProceedingForm(Model model, AddLegalProceedingRequest request) {
        model.addAttribute("proceeding", request);
        return "proceedings-form";
    }

    @GetMapping("/details/{id}")
    public String getDetailsOfProceeding(@PathVariable(name = "id") String id, Model model, Principal principal, @RequestParam(value = "errorMsg", required = false) String errorMsg) {
        LegalProceeding proceeding = legalProceedingService.getProceeding(id);
        model.addAttribute("errorMsg", errorMsg);
        model.addAttribute("proceeding", proceeding);
        model.addAttribute("owner", proceeding.getCreator() == accountService.getAccountFromPrincipal(principal));
        return "proceedings-details";
    }

    @PostMapping("/add")
    public String submitForm(AddLegalProceedingRequest request, Principal principal) {
        legalProceedingService.add(request, accountService.getAccountFromPrincipal(principal));
        return "redirect:/legalproceedings";
    }

    @GetMapping("/note/{id}")
    public String noteForm(Model model, @PathVariable("id") String id) {
        model.addAttribute("id", id);
        return "note-form";
    }

    @PostMapping("/note")
<<<<<<< HEAD
    public String submitNote(String note, String proceedingId, Principal principal) {
        legalProceedingService.addNote(proceedingId, note, accountService.getAccountFromPrincipal(principal));
        return "redirect:/legalproceedings";
=======
    public String submitNote(String notes, String legalProceedingsId, Principal principal) {
        legalProceedingService.addNote(legalProceedingsId, notes, accountService.getAccountFromPrincipal(principal));
        return "redirect:/legalproceedings/details/" + legalProceedingsId;
>>>>>>> 87d9cb2c2503b5ffa2cb181459c2404ccec6af72
    }

    @PostMapping("/advance")
    public String submitForm(double advance, String legalproceedingid) {
        legalProceedingService.setAdvance(legalproceedingid, advance);
        return "redirect:/legalproceedings/details/" + legalproceedingid;
    }

    @GetMapping("/complete")
    public String complete(String id, String stepId) {
        String errorMsg = "";
        try {
            legalProceedingService.complete(id, stepId);
        } catch (UnsupportedOperationException unsupportedOperationException) {
            errorMsg = "?errorMsg=This task cannot be closed yet";
        }
        return "redirect:/legalproceedings/details/" + id + errorMsg;
    }

<<<<<<< HEAD
=======
    @GetMapping("/fail")
    public String fail(String id, String stepId) {
        legalProceedingService.fail(id, stepId);
        return "redirect:/legalproceedings/details/" + id;
    }

>>>>>>> 87d9cb2c2503b5ffa2cb181459c2404ccec6af72

}
