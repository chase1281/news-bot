package com.example.newsBot.crawler;

import com.example.newsBot.domain.NewsArticle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class NaverNewsCrawlerTest {
    @Autowired
    private NaverNewsCrawler crawler;

    @Test
    void IT뉴스_크롤링_성공(){
        List<NewsArticle> articles = crawler.fetchNews("105", 10, "IT/과학");

        System.out.println("=== IT/과학 뉴스 ===");
        articles.forEach(a -> System.out.println(a.title() + "\n" + a.link() + "\n"));

        assertFalse(articles.isEmpty());
    }

    @Test
    void 경제뉴스_크롤링_성공(){
        List<NewsArticle> articles = crawler.fetchNews("101", 10, "경제");

        System.out.println("=== 경제 뉴스 ===");
        articles.forEach(a -> System.out.println(a.title() + "\n" + a.link() + "\n"));

        assertFalse(articles.isEmpty());
    }
}