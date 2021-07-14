package com.createam.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class OrderCreateResponse {

  @JsonAlias
  private String orderId;
  @JsonAlias
  private String redirectUri;

  private Status status;

  @JsonIgnore
  private String rawResponse;

  @Data
  public static class Status {
    public static final String STATUS_CODE_SUCCESS = "SUCCESS";
    @JsonAlias
    private String statusCode;
  }
}
