package pl.hack4law.lawrules.service.pdf;

import com.aspose.pdf.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.springframework.stereotype.Component;
import pl.hack4law.lawrules.model.AnonymizedDocument;
import pl.hack4law.lawrules.model.pdf.PresidioAnalyzerListResponse;
import pl.hack4law.lawrules.model.pdf.PresidioAnalyzerRequest;
import pl.hack4law.lawrules.model.pdf.PresidioAnalyzerResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PdfAnonymizer {
    private final AnalyzerClient analyzerClient;

    public Binary processFile(final AnonymizedDocument processedDocument){
        String fileContent = readFile(processedDocument.getFile());
        List<PresidioAnalyzerResponse> responseList = analyzerClient.sendAnalysisRequest(new PresidioAnalyzerRequest(fileContent, processedDocument.getLanguage().getLanguage()));

        Binary binaryObj = processedDocument.getFile();

        for (PresidioAnalyzerResponse presidioAnalyzerResponse : responseList) {
            String searchedPhrase = fileContent.substring(presidioAnalyzerResponse.getStart(), presidioAnalyzerResponse.getEnd());
            Document pdfDocument = openPDFDocument(binaryObj);
            TextFragmentAbsorber textFragmentAbsorber = new TextFragmentAbsorber(searchedPhrase);
            pdfDocument.getPages().accept(textFragmentAbsorber);
            TextFragmentCollection textFragmentCollection = textFragmentAbsorber.getTextFragments();

            for (TextFragment textFragment : (Iterable<TextFragment>) textFragmentCollection) {
                // Update text and other properties
                textFragment.setText(presidioAnalyzerResponse.getEntityType());
                textFragment.getTextState().setForegroundColor(Color.getBlack());
                textFragment.getTextState().setBackgroundColor(Color.getRed());
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            pdfDocument.save(byteArrayOutputStream);

            binaryObj = new Binary(byteArrayOutputStream.toByteArray());
        }

        return binaryObj;
    }

    public String readFile(Binary binaryObject){
        Document pdfDocument = openPDFDocument(binaryObject);

        com.aspose.pdf.TextAbsorber textAbsorber = new com.aspose.pdf.TextAbsorber();
        pdfDocument.getPages().accept(textAbsorber);

        // Get the extracted text
        return textAbsorber.getText();
    }

    private Document openPDFDocument(Binary binaryObject) {
        InputStream binaryInputStream = new ByteArrayInputStream(binaryObject.getData());

        Document pdfDocument = new Document(binaryInputStream);
        return pdfDocument;
    }
}
