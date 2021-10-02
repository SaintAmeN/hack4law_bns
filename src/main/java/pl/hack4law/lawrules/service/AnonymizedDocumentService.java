package pl.hack4law.lawrules.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.hack4law.lawrules.model.Account;
import pl.hack4law.lawrules.model.AnonymizedDocument;
import pl.hack4law.lawrules.model.dto.AnonymizedDocumentDto;
import pl.hack4law.lawrules.model.dto.UploadDocumentRequest;
import pl.hack4law.lawrules.repository.AnonymizedDocumentsRepository;
import pl.hack4law.lawrules.service.pdf.AnalyzerClient;
import pl.hack4law.lawrules.service.pdf.PdfAnonymizer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnonymizedDocumentService {
    private final AnonymizedDocumentsRepository anonymizedDocumentsRepository;
    private final AnalyzerClient analyzerClient;
    private final PdfAnonymizer pdfReader;

    public Boolean addDocument(UploadDocumentRequest request, Account owner) throws IOException {
        if (request.getDocument() == null) {
            log.info("is null.");
            return false;
        }
        if (request.getDocument().isEmpty()) {
            log.info("file is empty.");
            return false;
        }
        if (request.getDocument().getOriginalFilename() == null) {
            log.info("file name is null.");
            return false;
        }

        String fileName = StringUtils.cleanPath(request.getDocument().getOriginalFilename());

        Binary fileBinary = new Binary(BsonBinarySubType.BINARY, request.getDocument().getBytes());

        AnonymizedDocument document = AnonymizedDocument.builder()
                .file(fileBinary)
                .fileName(fileName)
                .name(request.getDocumentName())
                .language(request.getLanguage())
                .shared(request.sharingAllowed())
                .dateAdded(LocalDateTime.now())
                .owner(owner)
                .build();

        anonymizedDocumentsRepository.save(document);

        return null;
    }

    public List<AnonymizedDocumentDto> getDocuments(boolean shared, Account currentUser){
        List<AnonymizedDocument> documents = anonymizedDocumentsRepository.findAllByOwnerOrShared(currentUser, shared);
        log.info(String.format("Returned db result: %s", documents));
        return documents.stream()
                .map(this::mapToInfo)
                .collect(Collectors.toList());
    }


    public void anonymize(String documentId) throws IOException {
        Optional<AnonymizedDocument> documentOptional = anonymizedDocumentsRepository.findById(documentId);
        log.info(String.format("Returned db result: %s", documentOptional));

        if (documentOptional.isPresent()) {
            AnonymizedDocument document = documentOptional.get();
            Binary resultBinary = pdfReader.processFile(document);

            document.setAnonymized(resultBinary);
            anonymizedDocumentsRepository.save(document);
            log.info("Anonymized correctly.");
        }else{
            log.info("Failed to find document.");
        }
    }

    private AnonymizedDocumentDto mapToInfo(AnonymizedDocument document){
        return  AnonymizedDocumentDto.builder()
                .id(document.getId())
                .documentName(document.getName())
                .language(document.getLanguage())
                .documentFilename(document.getFileName())
                .sharingAllowed(document.isShared())
                .dateAdded(document.getDateAdded())
                .ownerUsername(document.getOwner().getUsername())
                .build();
    }

    public Binary getDocumentAnonymized(String documentId) {
        Optional<AnonymizedDocument> documentOptional = anonymizedDocumentsRepository.findById(documentId);
        log.info(String.format("Returned db result: %s", documentOptional));
        if (documentOptional.isPresent()) {
            AnonymizedDocument document = documentOptional.get();

            return document.getAnonymized();
        }
        throw new UnsupportedOperationException("Don't have that file");
    }
}
