package com.createam.payu;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.createam.payu.auth.AuthTokenRequestBuilder;

@RunWith(JUnit4.class)
public class AuthTokenRequestBuilderTest {

  public static final String TEST_PAYU_TOKEN_REQUEST_URI = "https://secure.payu.com/pl/standard/user/oauth/authorize?grant_type=client_credentials&client_id=145227&client_secret=12f071174cb7eb79d4aac5bc2f07563f";

  @Test
  public void shouldCreateCorrectPayuAuthRequest() {
    final String authorizationUri = "https://secure.payu.com/pl/standard/user/oauth/authorize";
    final String clientId = "145227";
    final String clientSecret = "12f071174cb7eb79d4aac5bc2f07563f";
    final String grantType = "client_credentials";

    final String createdUri = new AuthTokenRequestBuilder()
        .authorizationUri(authorizationUri)
        .clientId(clientId)
        .clientSecret(clientSecret)
        .grantType(grantType)
        .build();

    assertThat(TEST_PAYU_TOKEN_REQUEST_URI).isEqualTo(createdUri);
  }
}