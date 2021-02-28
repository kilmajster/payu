package com.createam.config;

import java.util.Collections;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.createam.payu.auth.AuthResponse;
import com.createam.payu.auth.AuthService;

@RequiredArgsConstructor
@Configuration
class PayUAuthConfig {

  private final AuthService authService;

  @Bean("payuApiRestTemplate")
  public RestTemplate payuRestTemplate() {
    final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    restTemplate.setInterceptors(Collections.singletonList((httpRequest, bytes, clientHttpRequestExecution) -> {
      final AuthResponse authResponse = authService.authenticate();
      final HttpHeaders headers = httpRequest.getHeaders();
      headers.add("Authorization", authResponse.getTokenType() + " " + authResponse.getAccessToken());
      if (!headers.containsKey("Content-Type")) {
        headers.add("Content-Type", "application/json");
      }
      return clientHttpRequestExecution.execute(httpRequest, bytes);
    }));

    return restTemplate;
  }
}