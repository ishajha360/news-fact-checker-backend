package com.isha.newsanalyzer.model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CredibilityScore {
    private int sourceReliability;
    private int factualAccuracy;
    private int biasLevel;
    private int claimVerifiability;
}
