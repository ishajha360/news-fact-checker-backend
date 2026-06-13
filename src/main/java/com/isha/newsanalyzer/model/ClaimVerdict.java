package com.isha.newsanalyzer.model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClaimVerdict {
    private String claim;
    private String status;
}
