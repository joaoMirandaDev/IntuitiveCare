package com.intuitive.care.controller;

import com.intuitive.care.service.WebScrapingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/webScraping")
public class WebScrapingController {

    private static final String TAG = "1° Etapa - WebScraping";

    @Autowired
    private WebScrapingService service;

    public WebScrapingController(WebScrapingService service) {
        this.service = service;
    }

    @RequestMapping(value = "/downloadAndCompressFiles", method = RequestMethod.POST, produces = "application/json")
    @Operation(summary = "Método utilizado para baixar os documentos e compactá-los atráves do Selenium",
            description = "Método utilizado para baixar os documentos e compactá-los atráves do Selenium", tags = TAG)
    public ResponseEntity<Void> downloadAndCompressFiles() throws IOException {
        service.downloadAndCompressFiles();
        return ResponseEntity.noContent().build();
    }

}

