package com.isha.newsanalyzer.model;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AnalysisResponse {
    private int overallScore;
    private String verdict;
    private CredibilityScore scores;
    private List<ClaimVerdict> claims;

}
