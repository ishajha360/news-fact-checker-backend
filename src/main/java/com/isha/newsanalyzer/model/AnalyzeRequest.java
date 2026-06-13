package com.isha.newsanalyzer.model;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnalyzeRequest {
    @NotBlank(message = "Text cannot be empty")
    private String text;
}
