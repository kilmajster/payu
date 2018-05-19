package com.createam.payu.order.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCreateResponse {

  @JsonAlias
  private String orderId;
  @JsonAlias
  private String redirectUri;

  private Status status;

  @Data
  public static class Status {
    public static final String STATUS_CODE_SUCCESS = "SUCCESS";
    @JsonAlias
    private String statusCode;
  }
}
