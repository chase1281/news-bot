package com.example.newsBot.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class KakaoTokenManager {

    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";

    private final RestTemplate restTemplate;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.refresh-token}")
    private String refreshToken;

    public String issueAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("refresh_token", refreshToken);

        ResponseEntity<Map> response = restTemplate.exchange(
                TOKEN_URL,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class
        );

        Map responseBody = response.getBody();
        if (responseBody == null || responseBody.get("access_token") == null) {
            throw new IllegalStateException("카카오 Access Token 발급 실패: " + responseBody);
        }

        Object newRefreshToken = responseBody.get("refresh_token");
        if (newRefreshToken != null) {
            log.warn("카카오 Refresh Token이 갱신되었습니다. GitHub Secrets를 업데이트하세요.");
        }

        return (String) responseBody.get("access_token");
    }
}
