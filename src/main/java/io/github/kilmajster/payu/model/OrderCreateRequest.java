package io.github.kilmajster.payu.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCreateRequest {
  private String continueUrl;
  private String customerIp;
  private String merchantPosId;
  private String description;
  private String currencyCode;
  private String totalAmount;
  private Buyer buyer;
  private List<Product> products;
}
