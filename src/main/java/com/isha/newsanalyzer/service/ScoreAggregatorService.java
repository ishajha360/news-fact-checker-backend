package com.isha.newsanalyzer.service;
import com.isha.newsanalyzer.model.CredibilityScore;
import org.springframework.stereotype.Service;

@Service
public class ScoreAggregatorService  {
    public int calculateOverallScore(CredibilityScore scores) {
        return (int)(
                scores.getSourceReliability()*0.30+
                        scores.getFactualAccuracy()*0.35 +
                        scores.getClaimVerifiability()*0.15 +
                        scores.getBiasLevel()*0.20

        );
    }
    public String getVerdict(int score){
        if(score >= 70) return "Mostly Credible";
        if(score >= 40) return "Mixed Signals";
        else return "Not Credible";
    }
}
