package com.isha.newsanalyzer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isha.newsanalyzer.model.AnalysisResponse;
import com.isha.newsanalyzer.model.ClaimVerdict;
import com.isha.newsanalyzer.model.CredibilityScore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FactCheckService {

    private final RestTemplate restTemplate;
    private final ScoreAggregatorService scoreAggregator;

    @Value("${openai.model}")
    private String model;

    private static final String OPENAI_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private static final String SYSTEM_PROMPT = """
            You are a fact-checking AI. Analyze the given article text.
            Return ONLY a JSON object, no explanation, no markdown, no extra text.
            
            Return exactly this structure:
            {
              "sourceReliability": <0-100>,
              "factualAccuracy": <0-100>,
              "biasLevel": <0-100>,
              "claimVerifiability": <0-100>,
              "claims": [
                { "claim": "short claim text", "status": "VERIFIED" or "MIXED" or "FALSE" }
              ]
            }
            
            Extract 2-4 key claims from the article. Be honest and strict with scores.
            """;

    public AnalysisResponse analyze(String articleText) {
        try {
            // OpenAI ko request bhejo
            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "system", "content", SYSTEM_PROMPT),
                            Map.of("role", "user", "content", articleText)
                    ),
                    "temperature", 0.2
            );

            // response lo
            Map response = restTemplate.postForObject(OPENAI_URL, requestBody, Map.class);

            // response se content string nikalo
            String content = (String) ((Map)((Map)
                    ((List) response.get("choices")).get(0))
                    .get("message")).get("content");

            // content string ko Java object mein convert karo
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(content);

            // CredibilityScore banao
            CredibilityScore scores = new CredibilityScore(
                    node.get("sourceReliability").asInt(),
                    node.get("factualAccuracy").asInt(),
                    node.get("biasLevel").asInt(),
                    node.get("claimVerifiability").asInt()
            );

            // claims list banao
            List<ClaimVerdict> claims = new ArrayList<>();
            for (JsonNode claimNode : node.get("claims")) {
                claims.add(new ClaimVerdict(
                        claimNode.get("claim").asText(),
                        claimNode.get("status").asText()
                ));
            }

            // overall score aur verdict nikalo
            int overallScore = scoreAggregator.calculateOverallScore(scores);
            String verdict = scoreAggregator.getVerdict(overallScore);

            return new AnalysisResponse(overallScore, verdict, scores, claims);

        } catch (Exception e) {
            throw new RuntimeException("Analysis failed: " + e.getMessage());
        }
    }
}
