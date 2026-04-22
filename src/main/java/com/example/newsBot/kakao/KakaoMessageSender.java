package com.example.newsBot.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class KakaoMessageSender {

    private static final String SEND_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private static final int MAX_TEXT_LENGTH = 2000;

    private final RestTemplate restTemplate;
    private final KakaoTokenManager tokenManager;

    public boolean send(String message) {
        if (message == null || message.isBlank()) {
            log.warn("빈 메시지 — 전송 중단");
            return false;
        }
        if (message.length() > MAX_TEXT_LENGTH) {
            log.warn("메시지 길이 {}자 — {}자 제한 초과", message.length(), MAX_TEXT_LENGTH);
            return false;
        }

        try {
            String accessToken = tokenManager.issueAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBearerAuth(accessToken);

            String template = buildTextTemplate(message);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("template_object", template);

            ResponseEntity<Map> response = restTemplate.exchange(
                    SEND_URL,
                    HttpMethod.POST,
                    new HttpEntity<>(body, headers),
                    Map.class
            );

            boolean success = response.getStatusCode().is2xxSuccessful();
            if (success) {
                log.info("카카오톡 전송 성공 ({}자)", message.length());
            } else {
                log.error("카카오톡 전송 실패: status={}, body={}",
                        response.getStatusCode(), response.getBody());
            }
            return success;
        } catch (Exception e) {
            log.error("카카오톡 전송 실패: {}", e.getMessage());
            return false;
        }
    }

    private String buildTextTemplate(String message) {
        String escaped = message
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");

        return "{\"object_type\":\"text\","
                + "\"text\":\"" + escaped + "\","
                + "\"link\":{\"web_url\":\"https://news.naver.com\","
                + "\"mobile_web_url\":\"https://news.naver.com\"},"
                + "\"button_title\":\"네이버 뉴스\"}";
    }
}
