package com.intuitive.care.controller;

import com.intuitive.care.service.TransformDataService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/transformData/")
public class TransformDataController {
    private static final String TAG = "TransformData";

    @Autowired
    private TransformDataService service;

    public TransformDataController(TransformDataService service) {
        this.service = service;
    }

    @RequestMapping(value = "/processPdfAndExportToCSV", method = RequestMethod.POST, produces = "application/json")
    @Operation(summary = "Método utilizado para extrair o anexo 1 do zip, converter em CSV e zipar o csv",
            description = "Método utilizado para extrair o anexo 1 do zip, converter em CSV e zipar o csv", tags = TAG)
    public ResponseEntity<Void> processPdfAndExportToCSV() throws IOException {
        service.processPdfAndExportToCSV();
        return ResponseEntity.noContent().build();
    }
}
