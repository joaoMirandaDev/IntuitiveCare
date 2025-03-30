package com.intuitive.care.WebScraping;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;


public class Automacao {

    private static final String ROUTE_DOWNLOAD_FILE = "/home/joao/Documentos";

    @Test
    public void search() throws InterruptedException {
        // Definir o caminho para o ChromeDriver
        System.setProperty("webdriver.chrome.driver", "src/drive/chromedriver");

        WebDriver driver = getWebDriver();

        Thread.sleep(2000);

        /* Este xpath e necessario caso precise aceitar os cookies, caso nao seja necessario remover o trecho abaixo */
        driver.findElement(By.xpath("/html/body/div[5]/div/div/div/div/div[2]/button[3]")).click();
        Thread.sleep(2000);

        /* Terá um intervalo de 2 segundos para que possa clicar em salvar o Anexo I e direciona-lo para a pasta adequada */
        driver.findElement(By.xpath("//*[@id=\"cfec435d-6921-461f-b85a-b425bc3cb4a5\"]/div/ol/li[1]/a[1]")).click();
        Thread.sleep(2000);

        /* Terá um intervalo de 2 segundos para que possa clicar em salvar o Anexo II e direciona-lo para a pasta adequada */
        driver.findElement(By.xpath("//*[@id=\"cfec435d-6921-461f-b85a-b425bc3cb4a5\"]/div/ol/li[2]/a")).click();

    }

    private static WebDriver getWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("download.default_directory", ROUTE_DOWNLOAD_FILE);

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", ROUTE_DOWNLOAD_FILE);
        prefs.put("plugins.always_open_pdf_externally", true);
        options.setExperimentalOption("prefs", prefs);

        WebDriver driver = new ChromeDriver(options);

        driver.get("https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos");
        return driver;
    }
}
