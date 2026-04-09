package com.example.newsBot.summarizer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NewsSummarizerTest {
    @Autowired
    private NewsSummarizer newsSummarizer;

    @Test
    void openAI_호출_성공(){
        String prompt = """
                오늘의 뉴스 목록입니다.
                IT/과학과 경제 섹션을 각각 두 줄씩 요약해주세요.
                형식: [키워드] 두 줄 요약
                링크: URL
                
                [IT/과학]
                1. AI가 슈퍼마리오 게임을 한다고?...광주과기원 국제대회 우승
                   https://n.news.naver.com/article/001/0001111111
                2. 호주 이어 인도네시아도 청소년 소셜미디어 금지
                   https://n.news.naver.com/article/001/0001111112
                           \s
                [경제]
                1. 미국 관세 우려에 코스피 0.8% 하락
                   https://n.news.naver.com/article/001/0001111116
                2. 한국은행, 기준금리 동결 결정
                https://n.news.naver.com/article/001/0001111117
                """;

        String result = newsSummarizer.summarize(prompt);
        System.out.println("=== OpenAI 응답 ===");
        System.out.println(result);

        assertNotNull(result);
    }
}