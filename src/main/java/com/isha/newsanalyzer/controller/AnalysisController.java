package com.isha.newsanalyzer.controller;

import com.isha.newsanalyzer.model.AnalysisResponse;
import com.isha.newsanalyzer.model.AnalyzeRequest;
import com.isha.newsanalyzer.service.FactCheckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnalysisController {

    private final FactCheckService factCheckService;

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResponse> analyze(
            @RequestBody @Valid AnalyzeRequest request) {
        AnalysisResponse response = factCheckService.analyze(request.getText());
        return ResponseEntity.ok(response);
    }
}
