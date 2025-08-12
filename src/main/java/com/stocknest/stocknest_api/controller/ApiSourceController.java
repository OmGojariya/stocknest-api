package com.stocknest.stocknest_api.controller;

import com.stocknest.stocknest_api.model.ApiSource;
import com.stocknest.stocknest_api.service.ApiSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sources")
@RequiredArgsConstructor
public class ApiSourceController {

    @Autowired
    private final ApiSourceService apiSourceService;

    @PostMapping
    public ApiSource saveSource(@RequestBody ApiSource source) {
        return apiSourceService.saveSource(source);
    }

    @GetMapping
    public List<ApiSource> getAllSources() {
        return apiSourceService.getAllSources();
    }

    @GetMapping("/{id}")
    public Optional<ApiSource> getSourceById(@PathVariable String id) {
        return apiSourceService.getSourceById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteSource(@PathVariable String id) {
        apiSourceService.deleteSource(id);
    }
}
