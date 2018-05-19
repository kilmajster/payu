package com.createam.payu.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

@Service
public class AuthService {

  @Value("${authorizationUri}")
  private String authorizationUri;

  @Value("${clientID}")
  private String clientId;

  @Value("${clientSecret}")
  private String clientSecret;

  private RestTemplate restTemplate = new RestTemplate();


  @SneakyThrows
  public AuthResponse authenticate() {
    final String authRequest = new AuthTokenRequestBuilder()
        .authorizationUri(authorizationUri)
        .clientId(clientId)
        .clientSecret(clientSecret)
        .grantType("client_credentials")
        .build();

    final ResponseEntity<String> jsonResponse = restTemplate.postForEntity(authRequest, null, String.class);
    return new ObjectMapper().readValue(jsonResponse.getBody(), AuthResponse.class);
  }
}
