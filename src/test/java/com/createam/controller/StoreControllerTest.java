package com.createam.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.createam.payu.order.OrderService;
import com.createam.payu.order.model.CheckoutForm;
import com.createam.payu.order.model.OrderCreateRequest;
import com.createam.payu.order.model.OrderCreateResponse;

@RunWith(MockitoJUnitRunner.class)
public class StoreControllerTest {

  private StoreController storeController;

  @Mock
  private OrderService orderService;

  @Mock
  private HttpServletRequest request;

  @Before
  public void setUp() {
    storeController = new StoreController(orderService);
  }

  @Test
  public void shouldCreateOrderWithCorrectEmail() {
    final String testEmail = "test@example.com";
    final CheckoutForm checkoutForm = new CheckoutForm();
    checkoutForm.setEmail(testEmail);
    when(request.getRemoteAddr()).thenReturn("localhost");
    final OrderCreateResponse.Status statusSucceed =  new OrderCreateResponse.Status();
    statusSucceed.setStatusCode("SUCCESS");
    final OrderCreateResponse mockedResponse = new OrderCreateResponse();
    mockedResponse.setStatus(statusSucceed);
    when(orderService.order(any())).thenReturn(mockedResponse);
    
    storeController.checkout(checkoutForm, request);

    ArgumentCaptor<OrderCreateRequest> capturedOrderRequest = ArgumentCaptor.forClass(OrderCreateRequest.class);
    verify(orderService, times(1)).order(capturedOrderRequest.capture());
    assertThat(capturedOrderRequest.getValue().getBuyer().getEmail()).isEqualTo(testEmail);
  }

  @Test
  public void shouldShowConfirmationView_whenPaymentSucceed() {
    final Optional<String> noError = Optional.empty();

    final String resultView = storeController.paymentFinish(noError);

    assertThat(resultView).isEqualTo("payment-confirmation");
  }

  @Test
  public void shouldShowErrorView_whenPaymentFailed() {
    final Optional<String> error = Optional.of("502");

    final String resultView = storeController.paymentFinish(error);

    assertThat(resultView).isEqualTo("payment-error");
  }
}
