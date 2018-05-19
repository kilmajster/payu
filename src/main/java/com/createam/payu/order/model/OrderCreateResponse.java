package com.createam.payu.order.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class OrderCreateResponse {

  @JsonAlias
  private String orderId;
  @JsonAlias
  private String redirectUri;

  private Status status;

  @Data
  public class Status {
    public static final String STATUS_CODE_SUCCESS = "SUCCESS";
    @JsonAlias
    private String statusCode;
  }
}
