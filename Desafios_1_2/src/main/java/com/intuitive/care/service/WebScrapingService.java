package com.intuitive.care.service;

import com.intuitive.care.utils.service.UtilsService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

@Service
public class WebScrapingService {

    private static final String ROUTE_DOWNLOAD_FILE = "/home/joao/Documentos";
    private static final String NAME_ZIP = "Anexos.zip";
    private static final String ANEXO_I = "Anexo_I_Rol_2021RN_465.2021_RN627L.2024.pdf";
    private static final String ANEXO_II = "Anexo_II_DUT_2021_RN_465.2021_RN628.2025_RN629.2025.pdf";
    private static final String URL_PAGE =
            "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";

    public void downloadAndCompressFiles() throws IOException {
        // Define o caminho para o ChromeDriver
        System.setProperty("webdriver.chrome.driver", "src/drive/chromedriver");
        WebDriver driver = UtilsService.configWebDriver(ROUTE_DOWNLOAD_FILE,URL_PAGE);

        /* Este xpath e necessario caso precise aceitar os cookies, caso nao seja necessario remover o trecho abaixo */
        driver.findElement(By.xpath("/html/body/div[5]/div/div/div/div/div[2]/button[3]")).click();

        Map<String, String> xpaths = new HashMap<>();
        xpaths.put("Anexo I", "//*[@id=\"cfec435d-6921-461f-b85a-b425bc3cb4a5\"]/div/ol/li[1]/a[1]");
        xpaths.put("Anexo II", "//*[@id=\"cfec435d-6921-461f-b85a-b425bc3cb4a5\"]/div/ol/li[2]/a");

        driver.findElement(By.xpath(xpaths.get("Anexo I"))).click();
        driver.findElement(By.xpath(xpaths.get("Anexo II"))).click();

        List<String> arquivos = List.of(
                ANEXO_I,
                ANEXO_II
        );

        arquivos.forEach(obj -> {
            try {
                UtilsService.waitForDownloadToComplete(ROUTE_DOWNLOAD_FILE, "/" + obj);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        UtilsService.compactToZip(arquivos, ROUTE_DOWNLOAD_FILE, NAME_ZIP);

        driver.quit();
    }
}
