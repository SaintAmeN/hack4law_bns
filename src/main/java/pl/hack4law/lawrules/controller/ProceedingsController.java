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
    public String getDetailsOfProceeding(@PathVariable(name = "id") String id, Model model, Principal principal) {
        LegalProceeding proceeding = legalProceedingService.getProceeding(id);
        model.addAttribute("proceeding", proceeding);
        model.addAttribute("owner", proceeding.getCreator() == accountService.getAccountFromPrincipal(principal));
        return "proceedings-details";
    }

    @PostMapping("/add")
    public String submitForm(AddLegalProceedingRequest request, Principal principal) {
        legalProceedingService.add(request, accountService.getAccountFromPrincipal(principal));
        return "redirect:/legalproceedings";
    }

    @PostMapping("/advance")
    public String submitForm(double advance, String legalproceedingid) {
        legalProceedingService.setAdvance(legalproceedingid, advance);
        return "redirect:/legalproceedings/details/" + legalproceedingid;
    }

    @GetMapping("/complete")
    public String complete(String id, String stepId) {
        legalProceedingService.complete(id,stepId);
        return "redirect:/legalproceedings/details/" + id;
    }


}
