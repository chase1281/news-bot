package com.example.newsBot.briefing;

import com.example.newsBot.domain.NewsArticle;
import com.example.newsBot.summarizer.SummaryParser.Section;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class MessageFormatter {

    private static final int KAKAO_TEXT_LIMIT = 2000;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MM/dd");

    public String format(List<NewsArticle> itArticles,
                         List<NewsArticle> economyArticles,
                         Map<Section, List<String>> summaries) {

        String today = LocalDate.now().format(DATE_FMT);

        StringBuilder sb = new StringBuilder();
        sb.append("📰 오늘의 뉴스 브리핑 (").append(today).append(")\n\n");

        appendTitleSection(sb, "💻 IT/과학", itArticles);
        appendTitleSection(sb, "📈 경제", economyArticles);

        if (summaries != null && !isEmpty(summaries)) {
            sb.append("🤖 AI 요약\n\n");
            appendSummarySection(sb, "[IT/과학]", summaries.get(Section.IT));
            appendSummarySection(sb, "[경제]", summaries.get(Section.ECONOMY));
        }

        return enforceLimit(sb.toString());
    }

    private void appendTitleSection(StringBuilder sb, String header, List<NewsArticle> articles) {
        if (articles == null || articles.isEmpty()) return;
        sb.append(header).append("\n");
        for (int i = 0; i < articles.size(); i++) {
            NewsArticle a = articles.get(i);
            sb.append(i + 1).append(". ").append(a.title()).append("\n");
            sb.append("   ").append(a.link()).append("\n");
        }
        sb.append("\n");
    }

    private void appendSummarySection(StringBuilder sb, String header, List<String> items) {
        if (items == null || items.isEmpty()) return;
        sb.append(header).append("\n");
        for (int i = 0; i < items.size(); i++) {
            sb.append(i + 1).append(". ").append(items.get(i)).append("\n");
        }
        sb.append("\n");
    }

    private boolean isEmpty(Map<Section, List<String>> summaries) {
        return summaries.values().stream().allMatch(v -> v == null || v.isEmpty());
    }

    private String enforceLimit(String message) {
        if (message.length() <= KAKAO_TEXT_LIMIT) return message;
        return message.substring(0, KAKAO_TEXT_LIMIT - 20) + "\n...(이하 생략)";
    }
}
