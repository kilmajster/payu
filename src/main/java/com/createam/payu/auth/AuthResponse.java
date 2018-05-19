package com.createam.payu.auth;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class AuthResponse {
  @JsonAlias("access_token")
  private String accessToken;
  @JsonAlias("token_type")
  private String tokenType;
  @JsonAlias("expires_in")
  private int expiresIn;
  @JsonAlias("grant_type")
  private String grantType;
  private String error;
  @JsonAlias("error_description")
  private String errorDescription;
}
