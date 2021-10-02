package pl.hack4law.lawrules.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.hack4law.lawrules.exception.RedirectToPageZero;
import pl.hack4law.lawrules.model.AnonymizedDocument;
import pl.hack4law.lawrules.model.DocumentLanguage;
import pl.hack4law.lawrules.model.dto.UploadDocumentRequest;
import pl.hack4law.lawrules.service.AccountService;
import pl.hack4law.lawrules.service.AnonymizedDocumentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;

@Slf4j
@Controller
@RequestMapping(path = "/documents")
@AllArgsConstructor
public class DocumentController {
    private final AnonymizedDocumentService anonymizedDocumentService;
    private final AccountService accountService;

    @PostMapping
    public String uploadDocument(Model model,
                                 UploadDocumentRequest request,
                                 Principal principal) throws IOException {
        log.info("Upload request: " + request);
        // TODO: handle result and notify about some missing data, remove model from parameters if not used
        Boolean result = anonymizedDocumentService.addDocument(request, accountService.getAccountFromPrincipal(principal));
        return "redirect:/documents";
    }

    @GetMapping
    public String documents(Model model,
                            boolean fetchShared,
                            Principal principal) {
        model.addAttribute("documents", anonymizedDocumentService.getDocuments(fetchShared, accountService.getAccountFromPrincipal(principal)));
        return "data-list";
    }

    @GetMapping("/upload")
    public String uploadForm(Model model, UploadDocumentRequest request) {
        model.addAttribute("uploadRequest", request);
        model.addAttribute("languages", DocumentLanguage.values());
        return "data-upload";
    }

    @GetMapping("/anonymize/{document_id}")
    public String anonymize(@PathVariable(name = "document_id") String documentId) throws IOException {
        // TODO: powiadomienia o tym czy byl sukces
        anonymizedDocumentService.anonymize(documentId);
        return "redirect:/documents";
    }

    @GetMapping("/download/{document_id}")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable(name = "document_id") String documentId) {
        Binary anonymizedDocument = anonymizedDocumentService.getDocumentAnonymized(documentId);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(anonymizedDocument.getData()));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=anonymized.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(anonymizedDocument.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
