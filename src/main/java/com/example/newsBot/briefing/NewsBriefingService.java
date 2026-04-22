package com.example.newsBot.briefing;

import com.example.newsBot.crawler.NaverNewsCrawler;
import com.example.newsBot.domain.NewsArticle;
import com.example.newsBot.summarizer.NewsSummarizer;
import com.example.newsBot.summarizer.PromptBuilder;
import com.example.newsBot.summarizer.SummaryParser;
import com.example.newsBot.summarizer.SummaryParser.Section;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsBriefingService {

    private static final String IT_SID = "105";
    private static final String ECONOMY_SID = "101";
    private static final String IT_CATEGORY = "IT/과학";
    private static final String ECONOMY_CATEGORY = "경제";
    private static final int LIMIT_PER_CATEGORY = 5;

    private final NaverNewsCrawler crawler;
    private final NewsSummarizer summarizer;
    private final PromptBuilder promptBuilder;
    private final SummaryParser summaryParser;
    private final MessageFormatter messageFormatter;

    public String buildBriefing() {
        List<NewsArticle> itArticles = crawler.fetchNews(IT_SID, LIMIT_PER_CATEGORY, IT_CATEGORY);
        List<NewsArticle> economyArticles = crawler.fetchNews(ECONOMY_SID, LIMIT_PER_CATEGORY, ECONOMY_CATEGORY);

        if (itArticles.isEmpty() && economyArticles.isEmpty()) {
            log.warn("IT/경제 모두 크롤링 실패 — 브리핑 생성 중단");
            return null;
        }

        Map<Section, List<String>> summaries = summarize(itArticles, economyArticles);
        return messageFormatter.format(itArticles, economyArticles, summaries);
    }

    private Map<Section, List<String>> summarize(List<NewsArticle> itArticles,
                                                 List<NewsArticle> economyArticles) {
        String prompt = promptBuilder.build(itArticles, economyArticles);
        String response = summarizer.summarize(prompt);

        if (response == null) {
            log.warn("OpenAI 응답 실패 — 제목만 전송하는 fallback으로 전환");
            return new EnumMap<>(Section.class);
        }
        return summaryParser.parse(response);
    }
}
