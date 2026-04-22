package com.example.newsBot;

import com.example.newsBot.briefing.NewsBriefingService;
import com.example.newsBot.kakao.KakaoMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@Slf4j
@RequiredArgsConstructor
public class NewsBotRunner implements CommandLineRunner {

    private final NewsBriefingService briefingService;
    private final KakaoMessageSender kakaoMessageSender;

    @Value("${newsbot.dry-run:false}")
    private boolean dryRun;

    @Override
    public void run(String... args) {
        log.info("뉴스 브리핑 파이프라인 시작 (dry-run={})", dryRun);

        String message = briefingService.buildBriefing();
        if (message == null) {
            log.error("브리핑 메시지 생성 실패 — 종료");
            return;
        }

        log.info("\n----- 메시지 미리보기 -----\n{}\n---------------------------", message);

        if (dryRun) {
            log.info("dry-run 모드 — 카카오톡 전송을 건너뜁니다");
            return;
        }

        kakaoMessageSender.send(message);
    }
}
