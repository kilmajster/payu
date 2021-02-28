package com.createam.payu.order;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.createam.payu.order.model.OrderCreateRequest;
import com.createam.payu.order.model.OrderCreateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService {

  @Value("${payuOrderUrl}")
  private String payuOrderUrl;

  @Resource(name = "payuApiRestTemplate")
  private RestTemplate restTemplate;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @SneakyThrows
  public OrderCreateResponse order(final OrderCreateRequest orderCreateRequest) {
    final ResponseEntity<String> jsonResponse = restTemplate.postForEntity(payuOrderUrl, orderCreateRequest, String.class);

    log.info("Response as String = {}", jsonResponse.getBody());

    return objectMapper.readValue(jsonResponse.getBody(), OrderCreateResponse.class);
  }
}
