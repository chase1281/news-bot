package com.example.newsBot;

import com.example.newsBot.summarizer.NewsSummarizer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class NewsBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsBotApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	public CommandLineRunner test(NewsSummarizer summarizer){
		return args -> {
			String result = summarizer.summarize("안녕하세요, 한 줄로 자기소개 해줘.");
			System.out.println("=== OpenAI 응답 ===");
			System.out.println(result);
		};
	}
}
