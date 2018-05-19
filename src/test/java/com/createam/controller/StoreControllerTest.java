package com.createam.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.createam.payu.order.OrderService;

@RunWith(MockitoJUnitRunner.class)
public class StoreControllerTest {

  private StoreController storeController;

  @Mock
  private OrderService orderService;

  @Before
  public void setUp() {
    storeController = new StoreController(orderService);
  }

  @Test
  public void

  @Test
  public void shouldShowConfirmationView_whenPaymentSucceed() {
    final Optional<String> noError = Optional.empty();

    final String resultView = storeController.paymentFinish(noError);

    assertThat(resultView).isEqualTo("payment-confirmation");
  }

  @Test
  public void shouldShowErrorView_whenPaymentSucceed() {
    final Optional<String> error = Optional.of("502");

    final String resultView = storeController.paymentFinish(error);

    assertThat(resultView).isEqualTo("payment-error");
  }
}