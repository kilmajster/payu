package com.createam.payu.order.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckoutForm {
  private String email;
}
