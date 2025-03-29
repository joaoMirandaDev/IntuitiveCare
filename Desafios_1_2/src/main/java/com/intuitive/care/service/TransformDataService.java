package com.intuitive.care.service;

import com.intuitive.care.utils.service.UtilsService;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.*;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.*;
import java.util.*;
import java.util.zip.*;

@Service
public class TransformDataService {

    private static final String directory = "/home/joao/Documentos/";
    private static final String zip = "Anexos.zip";
    private static final String pdfFilename = "Anexo_I_Rol_2021RN_465.2021_RN627L.2024.pdf";

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

        UtilsService.extractPdfFromZip(directory + zip, pdfFilename, directory);

        // Caminho do arquivo PDF
        File pdfFile = new File(directory+pdfFilename);

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
        String csvFilePath = directory+"Teste_Joao_Victor.csv";

        // Criando e escrevendo os dados no arquivo CSV
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            // Escrevendo todas as linhas coletadas no CSV
            writer.writeAll(data);
        }
        List<String> arquivos = List.of("Teste_Joao_Victor.csv");

        UtilsService.compactToZip(arquivos,directory, "Teste_Joao_Victor.zip");
    }
}
