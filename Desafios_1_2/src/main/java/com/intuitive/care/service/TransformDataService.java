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

    // Função para substituir valores
    public String replaceValue(String value) {
        if (value.equals("OD")) {
            return "Seg. Odontológica";
        } else if (value.equals("AMB")) {
            return "Seg. Ambulatorial";
        } else {
            return value;
        }
    }

    // Função para processar o PDF e exportar os dados para um arquivo CSV
    public void processPdfAndExportToCSV() throws IOException {
        Integer count = 0;
        UtilsService.extractPdfFromZip(directory.concat(zip), pdfFilename, directory);

        // Caminho do arquivo PDF
        File pdfFile = new File(directory.concat(pdfFilename));

        // Criação de uma lista para armazenar as linhas extraídas
        List<String[]> data = new ArrayList<>();

        try (PDDocument document = PDDocument.load(pdfFile)) {
            // Usar SpreadsheetExtractionAlgorithm para extrair dados de tabela
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();

            // Usar ObjectExtractor para extrair páginas
            ObjectExtractor extractor = new ObjectExtractor(document);
            PageIterator pi = extractor.extract();

            while (pi.hasNext()) {
                // Iterando sobre as páginas do documento
                Page page = pi.next();

                // Extrair as tabelas usando SpreadsheetExtractionAlgorithm
                List<Table> tables = sea.extract(page);

                // Iterando sobre as tabelas na página
                for (Table table : tables) {
                    List<List<RectangularTextContainer>> rows = table.getRows();

                    // Iterando sobre as linhas e células para coletar o conteúdo
                    for (List<RectangularTextContainer> cells : rows) {
                        List<String> row = new ArrayList<>();
                        System.out.println("Adicionando linhas no CSV:" + count++);
                        // Iterando sobre as células e extraindo o texto
                        for (RectangularTextContainer cell : cells) {
                            // Substituindo \r por espaços
                            String text = cell.getText().replace("\r", " ");
                            // Substituindo valores conforme necessário (OD -> Seg. Odontológica e AMB -> Seg. Ambulatorial)
                            text = replaceValue(text);
                            row.add(text);
                        }

                        // Adicionando a linha ao conjunto de dados
                        data.add(row.toArray(new String[0]));
                    }
                }
            }
        }

        // Caminho do arquivo CSV para salvar os dados extraídos
        String csvFilePath = directory.concat(anexoCsv);

        // Criando e escrevendo os dados no arquivo CSV
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            // Escrevendo todas as linhas coletadas no CSV
            writer.writeAll(data);
        }
        List<String> arquivos = List.of(anexoCsv);

        UtilsService.compactToZip(arquivos,directory, "Teste_Joao_Victor.zip");

        List<String> filesDelete = List.of(directory.concat(anexoCsv), directory.concat(pdfFilename));
        UtilsService.deleteFilesByPath(filesDelete);
    }
}
