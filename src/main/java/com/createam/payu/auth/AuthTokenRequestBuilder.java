package com.createam.payu.auth;


public class AuthTokenRequestBuilder {

  private String authorizationUri;
  private String clientId;
  private String clientSecret;
  private String grantType;

  public AuthTokenRequestBuilder authorizationUri(final String authorizationUri) {
    this.authorizationUri = authorizationUri;
    return this;
  }

  public AuthTokenRequestBuilder clientId(final String clientId) {
    this.clientId = clientId;
    return this;
  }

  public AuthTokenRequestBuilder clientSecret(final String clientSecret) {
    this.clientSecret = clientSecret;
    return this;
  }

  public AuthTokenRequestBuilder grantType(final String grantType) {
    this.grantType = grantType;
    return this;
  }

  public String build() {
    return new StringBuilder(authorizationUri)
        .append("?grant_type=").append(grantType)
        .append("&client_id=").append(clientId)
        .append("&client_secret=").append(clientSecret)
        .toString();
  }
}
