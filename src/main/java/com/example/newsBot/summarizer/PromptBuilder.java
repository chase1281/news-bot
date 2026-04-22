package com.example.newsBot.summarizer;

import com.example.newsBot.domain.NewsArticle;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromptBuilder {

    public String build(List<NewsArticle> itArticles, List<NewsArticle> economyArticles) {
        StringBuilder sb = new StringBuilder();

        sb.append("아래는 오늘의 IT/과학 뉴스와 경제 뉴스 제목 목록입니다.\n");
        sb.append("각 기사를 1~3줄로 핵심만 요약해주세요.\n\n");

        sb.append("[응답 형식]\n");
        sb.append("- 섹션은 반드시 [IT/과학] 과 [경제] 두 개로 구분합니다.\n");
        sb.append("- 각 섹션 안에서는 입력된 번호 순서를 유지합니다.\n");
        sb.append("- 각 항목은 \"N. [키워드] 1~3줄 요약\" 형식입니다.\n");
        sb.append("- 키워드는 4~8자 이내 한글로 기사의 핵심 토픽을 담습니다.\n");
        sb.append("- 링크, 군더더기 인사말, 코드블록 없이 위 형식만 출력합니다.\n\n");

        sb.append("[예시]\n");
        sb.append("[IT/과학]\n");
        sb.append("1. [AI 게임] 광주과기원 팀이 AI 게임 플레이 국제대회에서 우승했다.\n\n");
        sb.append("[경제]\n");
        sb.append("1. [증시] 미국 관세 우려에 코스피가 0.8% 하락했다.\n\n");

        sb.append("[뉴스 목록]\n");
        appendSection(sb, "[IT/과학]", itArticles);
        sb.append("\n");
        appendSection(sb, "[경제]", economyArticles);

        return sb.toString();
    }

    private void appendSection(StringBuilder sb, String header, List<NewsArticle> articles) {
        sb.append(header).append("\n");
        if (articles.isEmpty()) {
            sb.append("(수집된 기사가 없습니다)\n");
            return;
        }
        for (int i = 0; i < articles.size(); i++) {
            sb.append(i + 1).append(". ").append(articles.get(i).title()).append("\n");
        }
    }
}
