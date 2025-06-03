package com.intuitive.care.controller;

import com.intuitive.care.service.TransformDataService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/transformData")
public class TransformDataController {
    private static final String TAG = "2° Etapa - TransformData";

    @Autowired
    private TransformDataService service;

    public TransformDataController(TransformDataService service) {
        this.service = service;
    }

    @RequestMapping(value = "/processPdfAndExportToCSV", method = RequestMethod.POST, produces = "application/json")
    @Operation(summary = "Método utilizado para extrair o anexo 1 do zip, converter em CSV e zipar o csv",
            description = "Método utilizado para extrair o anexo 1 do zip, converter em CSV e zipar o csv", tags = TAG)
    public ResponseEntity<Void> processPdfAndExportToCSV(@RequestParam("file") MultipartFile file) throws IOException {
        service.processPdfAndExportToCSV(file);
        return ResponseEntity.noContent().build();
    }
}
