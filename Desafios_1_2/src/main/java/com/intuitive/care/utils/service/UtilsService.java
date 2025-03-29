package com.intuitive.care.utils.service;

import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Service
public class UtilsService {
    public static void compactToZip(List<String> arquivosEntrada, String routeFile, String nameZip) throws IOException {

        byte[] dados = new byte[8192];

        try (FileOutputStream fileOutputStream = new FileOutputStream(routeFile + "/" + nameZip);
             ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {

            for (String arquivo : arquivosEntrada) {
                File file = new File(routeFile + "/" + arquivo);

                if (!file.exists() || file.isDirectory()) {
                    throw new NotFoundException("Arquivo não encontrado ou é um diretório: " + arquivo);
                }

                try (FileInputStream fileInputStream = new FileInputStream(file);
                     BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream, 8192)) {

                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zipOutputStream.putNextEntry(zipEntry);

                    int bytesLidos;
                    while ((bytesLidos = bufferedInputStream.read(dados)) != -1) {
                        zipOutputStream.write(dados, 0, bytesLidos);
                    }
                    zipOutputStream.closeEntry();
                }
            }

        } catch (IOException e) {
            throw new IOException("Erro ao compactar arquivos para ZIP: " + e.getMessage(), e);
        }
    }

    public static WebDriver configWebDriver(String routeFile, String urlPage) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("download.default_directory", routeFile);

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", routeFile);
        prefs.put("plugins.always_open_pdf_externally", true);
        options.setExperimentalOption("prefs", prefs);

        WebDriver driver = new ChromeDriver(options);

        driver.get(urlPage);
        return driver;
    }

    public static void deleteFilesByPath(List<String> route) throws IOException {
        route.forEach(obj -> {
            Path path = Paths.get(obj);
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }


    public static void waitForDownloadToComplete(String downloadPath, String fileName) throws InterruptedException, IOException {
        File file = new File(downloadPath + fileName);
        int timeout = 60;
        int elapsed = 0;

        while (!file.exists() && elapsed < timeout) {
            Thread.sleep(1000);
            elapsed++;
        }

        if (!file.exists()) {
            throw new RuntimeException("O arquivo " + fileName + " não foi baixado no tempo esperado.");
        }
    }


    // Função para extrair PDF do arquivo ZIP
    public static void extractPdfFromZip(String zipPath,String pdfFilename,String outputDir) throws IOException {
        try (ZipFile zipFile = new ZipFile(zipPath)) {
            ZipEntry entry = zipFile.getEntry(pdfFilename);
            if (entry != null) {
                File outputFile = new File(outputDir + pdfFilename);
                try (InputStream inputStream = zipFile.getInputStream(entry);
                     OutputStream outputStream = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                }
            } else {
                throw new FileNotFoundException("PDF file not found inside the ZIP.");
            }
        }
    }

};
