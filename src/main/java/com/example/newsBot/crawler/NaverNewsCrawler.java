package com.example.newsBot.crawler;

import com.example.newsBot.domain.NewsArticle;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

@Component
@Slf4j
public class NaverNewsCrawler {
    public List<NewsArticle> fetchNews(String sid, int limit, String category){
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String url = "https://news.naver.com/main/list.naver?mode=LS2D&mid=shm&sid1=" + sid
                + "&date=" + today;
        try{
            Document doc = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                            .timeout(5000)
                            .get();

            LinkedHashSet<String> seen = new LinkedHashSet<>();
            List<NewsArticle> articles = new ArrayList<>();

            Elements elements = doc.select("ul.type06_headline li dt a, ul.type06 li dt a");

            for(Element el: elements){
                String title = el.text().trim();
                String link = el.attr("href");

                if(title.isEmpty() || link.isEmpty()) continue;
                if(!seen.add(title)) continue;

                articles.add(new NewsArticle(title, link, category));

                if(articles.size() >= limit) break;
            }

            log.info("[{}] 크롤링 완료: {}건", category, articles.size());
            return articles;
        }catch (Exception e){
            log.error("[{}] 크롤링 실패: {}", category, e.getMessage());
            return Collections.emptyList();
        }
    }
}
