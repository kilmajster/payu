package io.github.kilmajster.payu.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
  private String name;
  private String unitPrice;
  private String quantity;
}
