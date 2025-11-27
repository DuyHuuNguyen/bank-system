package com.bank.transaction_service.infrastructure.config;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebclientConfig {

  @Bean("client-wallet-service")
  public WebClient webClient() {
    HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(10));

    return WebClient.builder()
        .baseUrl("http://localhost:8038")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .build();
  }
}
