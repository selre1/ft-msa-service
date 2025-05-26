package com.ftcompany.catalogservice;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogService catalogService;
    private final CatalogRepository catalogRepository;
    private final ModelMapper modelMapper;

    @Value("${server.port}")
    private String port;

    @GetMapping("/health_check")
    public String status(){
        return String.format("catalog Service Port %s",port);
    }

    @GetMapping("/all")
    private ResponseEntity<List<ResponseCatalog>> getAll(){
        List<Catalog> catalogs = catalogRepository.findAll();

        List<ResponseCatalog> responseCatalogs = new ArrayList<>();

        catalogs.forEach(catalog -> {
            responseCatalogs.add(modelMapper.map(catalog, ResponseCatalog.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(responseCatalogs);
    }
}
