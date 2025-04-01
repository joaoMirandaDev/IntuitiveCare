package com.intuitive.care.service;

import com.intuitive.care.utils.service.UtilsService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import technology.tabula.*;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.*;
import java.util.*;

@Service
@PropertySource("classpath:application.properties")
public class TransformDataService {

    @Value("${directory}")
    private String directory;
    @Value("${name.zip}")
    private String zip;
    @Value("${anexo.I}")
    private String pdfFilename;
    @Value("${anexo.csv}")
    private String anexoCsv;

    // Metodo para substituir valores de OD E AMB
    public String replaceValue(String value) {
        if (value.equals("OD")) {
            return "Seg. Odontológica";
        } else if (value.equals("AMB")) {
            return "Seg. Ambulatorial";
        } else {
            return value;
        }
    }

    // Metodo para extrair dados do PDF e exportar para um arquivo CSV
    public void processPdfAndExportToCSV() throws IOException {
        UtilsService.extractPdfFromZip(directory.concat(zip), pdfFilename, directory);

        File pdfFile = new File(directory.concat(pdfFilename));

        List<String[]> data = new ArrayList<>();

        try (PDDocument document = PDDocument.load(pdfFile)) {
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();

            ObjectExtractor extractor = new ObjectExtractor(document);
            PageIterator pi = extractor.extract();

            while (pi.hasNext()) {
                Page page = pi.next();

                List<Table> tables = sea.extract(page);

                for (Table table : tables) {
                    List<List<RectangularTextContainer>> rows = table.getRows();
                    for (List<RectangularTextContainer> cells : rows) {
                        List<String> row = new ArrayList<>();
                        for (RectangularTextContainer cell : cells) {
                            String text = cell.getText().replace("\r", " ");
                            text = replaceValue(text);
                            row.add(text);
                        }
                        data.add(row.toArray(new String[0]));
                    }
                }
            }
        }
        String csvFilePath = directory.concat(anexoCsv);
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            writer.writeAll(data);
        }
        UtilsService.compactToZip(List.of(anexoCsv),directory, "Teste_Joao_Victor.zip");

        UtilsService.deleteFilesByPath(List.of(directory.concat(anexoCsv), directory.concat(pdfFilename)));
    }
}
