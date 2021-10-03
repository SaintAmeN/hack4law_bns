package pl.hack4law.lawrules.service.pdf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;
import org.bson.types.Binary;
import org.springframework.stereotype.Component;
import pl.hack4law.lawrules.model.AnonymizedDocument;
import pl.hack4law.lawrules.model.pdf.PresidioAnalyzerListResponse;
import pl.hack4law.lawrules.model.pdf.PresidioAnalyzerRequest;
import pl.hack4law.lawrules.model.pdf.PresidioAnalyzerResponse;

import javax.xml.soap.Text;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PdfAnonymizer {
    private final AnalyzerClient analyzerClient;

    public Binary processFile(final AnonymizedDocument processedDocument) throws IOException {
        String fileContent = readFile(processedDocument.getFile());
        log.info(fileContent);
        List<PresidioAnalyzerResponse> responseList = analyzerClient.sendAnalysisRequest(new PresidioAnalyzerRequest(fileContent, processedDocument.getLanguage().getLanguage()));

        PDDocument documentRewrite = PDDocument.load(processedDocument.getFile().getData());
        List<TextPositionSequence>[] tpss = new List[documentRewrite.getNumberOfPages()];
        for (int i = 0; i < documentRewrite.getNumberOfPages(); i++) {
            tpss[i] = new ArrayList<>();
        }
        documentRewrite.save("file.pdf");
        documentRewrite.close();

        for (PresidioAnalyzerResponse presidioAnalyzerResponse : responseList) {
            String searchedPhrase = fileContent.substring(presidioAnalyzerResponse.getStart(), presidioAnalyzerResponse.getEnd());
            log.info("Poszujuje frazy: " + searchedPhrase);

            PDDocument documentRead = PDDocument.load(new File("file.pdf"));
            for (int page = 0; page < documentRead.getNumberOfPages(); page++) {
                List<TextPositionSequence> hits = findSubwords(documentRead, page, searchedPhrase);
                for (TextPositionSequence hit : hits) {
                    tpss[page].add(hit);
                    for (TextPosition lastPosition : hit.textPositions) {
                        log.info("Fraza w lokalizacji: " + lastPosition);
                        log.info(String.format("Page %s at %s, %s with width %s and last letter '%s' at %s, %s\n",
                                page, hit.getX(), hit.getY(), hit.getWidth(),
                                lastPosition.getUnicode(), lastPosition.getXDirAdj(), lastPosition.getYDirAdj()));

                    }
                }
            }
            documentRead.close();
        }


        int i = 0;
        for (List<TextPositionSequence> tps : tpss) {
            for (TextPositionSequence hit : tps) {
                PDDocument documentWrite = PDDocument.load(new File("file.pdf"));
                PDPageContentStream contentStream = new PDPageContentStream(documentWrite, documentWrite.getPage(i), PDPageContentStream.AppendMode.APPEND, false);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.addRect(hit.textPositions.get(0).getX(), hit.textPositions.get(0).getY(), hit.textPositions.get(hit.textPositions.size()-1).getX()-hit.textPositions.get(0).getX(), 30);
                contentStream.fill();
                contentStream.close();
                documentWrite.save("file.pdf");
                documentWrite.close();
            }
            i++;
        }

        return processedDocument.getFile();
    }

//    protected void writeString(PDDocument document, String string, List<TextPosition> textPositions) throws IOException {
//        float posXInit = 0,
//                posXEnd = 0,
//                posYInit = 0,
//                posYEnd = 0,
//                width = 0,
//                height = 0,
//                fontHeight = 0;
//
//        posXInit = textPositions.get(0).getXDirAdj();
//        posXEnd = textPositions.get(textPositions.size() - 1).getXDirAdj() + textPositions.get(textPositions.size() - 1).getWidth();
//        posYInit = textPositions.get(0).getPageHeight() - textPositions.get(0).getYDirAdj();
//        posYEnd = textPositions.get(0).getPageHeight() - textPositions.get(textPositions.size() - 1).getYDirAdj();
//        width = textPositions.get(0).getWidthDirAdj();
//        height = textPositions.get(0).getHeightDir();
//
//        System.out.println(string + "X-Init = " + posXInit + "; Y-Init = " + posYInit + "; X-End = " + posXEnd + "; Y-End = " + posYEnd + "; Font-Height = " + fontHeight);
//
//        float quadPoints[] = {posXInit, posYEnd + height + 2, posXEnd, posYEnd + height + 2, posXInit, posYInit - 2, posXEnd, posYEnd - 2};
//
//        List<PDAnnotation> annotations = document.getPage(document.get() - 1).getAnnotations();
//        PDAnnotationTextMarkup highlight = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT);
//
//        PDRectangle position = new PDRectangle();
//        position.setLowerLeftX(posXInit);
//        position.setLowerLeftY(posYEnd);
//        position.setUpperRightX(posXEnd);
//        position.setUpperRightY(posYEnd + height);
//
//        highlight.setRectangle(position);
//
//        // quadPoints is array of x,y coordinates in Z-like order (top-left, top-right, bottom-left,bottom-right)
//        // of the area to be highlighted
//
//        highlight.setQuadPoints(quadPoints);
//
//        PDColor yellow = new PDColor(new float[]{1, 1, 1 / 255F}, PDDeviceRGB.INSTANCE);
//        highlight.setColor(yellow);
//        annotations.add(highlight);
//
//    }

    List<TextPositionSequence> findSubwords(PDDocument document, int page, String searchTerm) throws IOException {
        final List<TextPositionSequence> hits = new ArrayList<TextPositionSequence>();
        PDFTextStripper stripper = new PDFTextStripper() {
            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                TextPositionSequence word = new TextPositionSequence(textPositions);
                String string = word.toString();

                int fromIndex = 0;
                int index;
                while ((index = string.indexOf(searchTerm, fromIndex)) > -1) {
                    hits.add(word.subSequence(index, index + searchTerm.length()));
                    fromIndex = index + 1;
                }
                super.writeString(text, textPositions);
            }
        };

        stripper.setSortByPosition(true);
//        stripper.setStartPage(page);
//        stripper.setEndPage(page);
//        stripper.setP
        stripper.getText(document);
        return hits;
    }

    public String readFile(Binary binaryObject) throws IOException {
        PDDocument doc = PDDocument.load(binaryObject.getData());
        StringBuilder stringBuilder = new StringBuilder();
        if (!doc.isEncrypted()) {

            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);

            PDFTextStripper tStripper = new PDFTextStripper();

            String pdfFileInText = tStripper.getText(doc);
            stringBuilder.append(pdfFileInText);
        }

        doc.close();
        return stringBuilder.toString();
    }

//    public Binary processFile(final AnonymizedDocument processedDocument){
//        String fileContent = readFile(processedDocument.getFile());
//        List<PresidioAnalyzerResponse> responseList = analyzerClient.sendAnalysisRequest(new PresidioAnalyzerRequest(fileContent, processedDocument.getLanguage().getLanguage()));
//
//        Binary binaryObj = processedDocument.getFile();
//
//        for (PresidioAnalyzerResponse presidioAnalyzerResponse : responseList) {
//            String searchedPhrase = fileContent.substring(presidioAnalyzerResponse.getStart(), presidioAnalyzerResponse.getEnd());
//            Document pdfDocument = openPDFDocument(binaryObj);
//            TextFragmentAbsorber textFragmentAbsorber = new TextFragmentAbsorber(searchedPhrase);
//            pdfDocument.getPages().accept(textFragmentAbsorber);
//            TextFragmentCollection textFragmentCollection = textFragmentAbsorber.getTextFragments();
//
//            for (TextFragment textFragment : (Iterable<TextFragment>) textFragmentCollection) {
//                // Update text and other properties
//                textFragment.setText(presidioAnalyzerResponse.getEntityType());
//                textFragment.getTextState().setForegroundColor(Color.getBlack());
//                textFragment.getTextState().setBackgroundColor(Color.getRed());
//            }
//
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            pdfDocument.save(byteArrayOutputStream);
//
//            binaryObj = new Binary(byteArrayOutputStream.toByteArray());
//        }
//
//        return binaryObj;
//    }
//
//    public String readFile(Binary binaryObject){
//        Document pdfDocument = openPDFDocument(binaryObject);
//
//        TextFragmentAbsorber textFragmentAbsorber = new TextAbsorber();
//        pdfDocument.getPages().accept(textFragmentAbsorber);
//        TextFragmentCollection textFragmentCollection = textFragmentAbsorber.getTextFragments();
//
//        StringBuilder builder = new StringBuilder();
//        for (TextFragment textFragment : (Iterable<TextFragment>) textFragmentCollection) {
//            // Update text and other properties
//            builder.append());
//        }
//        // Get the extracted text
//        return builder.toString();
//    }
//
//    private Document openPDFDocument(Binary binaryObject) {
//        InputStream binaryInputStream = new ByteArrayInputStream(binaryObject.getData());
//
//        Document pdfDocument = new Document(binaryInputStream);
//        return pdfDocument;
//    }
}
