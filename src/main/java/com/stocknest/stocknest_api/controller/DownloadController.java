package com.stocknest.stocknest_api.controller;

import com.stocknest.stocknest_api.model.DownloadRequest;
import com.stocknest.stocknest_api.service.DownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/downloads")
@RequiredArgsConstructor
public class DownloadController {

    @Autowired
    private final DownloadService downloadService;

    @PostMapping
    public DownloadRequest saveDownload(@RequestBody DownloadRequest request) {
        return downloadService.saveRequest(request);
    }

    @GetMapping("/user/{userId}")
    public List<DownloadRequest> getDownloadsByUser(@PathVariable String userId) {
        return downloadService.getRequestsByUserId(userId);
    }
}
