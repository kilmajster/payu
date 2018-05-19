package com.createam.controller;

import static com.createam.payu.order.model.OrderCreateResponse.Status.STATUS_CODE_SUCCESS;
import static java.util.Arrays.asList;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.createam.payu.order.OrderService;
import com.createam.payu.order.model.Buyer;
import com.createam.payu.order.model.CheckoutForm;
import com.createam.payu.order.model.OrderCreateRequest;
import com.createam.payu.order.model.OrderCreateResponse;
import com.createam.payu.order.model.Product;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class StoreController {

  public static final String URL_INDEX = "/";
  public static final String PAGE_INDEX = "index";
  public static final String URL_PAYMENT = "/payment";
  public static final String PAGE_PAYMENT_ERROR = "payment-error";
  public static final String PAGE_PAYMENT_CONFIRMATION = "payment-confirmation";
  public static final String URL_CHECKOUT = "/checkout";

  @Value("${server.addr}")
  private String serverAddress;

  @Value("${merchantPosId}")
  private String merchantPosId;

  @Value("${storeName}")
  private String storeName;

  private final OrderService orderService;

  @Autowired
  public StoreController(final OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping(URL_INDEX)
  public String index(final Model model) {
    model.addAttribute(CheckoutForm.builder().build());
    return PAGE_INDEX;
  }

  @PostMapping(URL_CHECKOUT)
  public RedirectView checkout(final @ModelAttribute CheckoutForm checkoutForm, final HttpServletRequest request) {
    final OrderCreateRequest orderRequest = prepareOrderCreateRequest(checkoutForm, request);
    final OrderCreateResponse response = orderService.order(orderRequest);

    if (!response.getStatus().getStatusCode().equals(STATUS_CODE_SUCCESS)) {
      throw new RuntimeException("Payment failed! ");
    }
    return new RedirectView(response.getRedirectUri());
  }

  @GetMapping(URL_PAYMENT)
  public String paymentFinish(final @RequestParam Optional<String> error) {
    error.ifPresent(e -> log.error("ERROR CODE - " + e));
    return error.isPresent() ? PAGE_PAYMENT_ERROR : PAGE_PAYMENT_CONFIRMATION;
  }

  private OrderCreateRequest prepareOrderCreateRequest(
      final CheckoutForm checkoutForm, final HttpServletRequest request) {
    return OrderCreateRequest.builder()
        .customerIp(request.getRemoteAddr())
        .continueUrl(serverAddress + URL_PAYMENT)
        .merchantPosId(merchantPosId)
        .description(storeName)
        .currencyCode("PLN")
        .totalAmount("2500")
        .products(asList(Product.builder()
            .name("karta podarunkowa")
            .quantity("1")
            .unitPrice("2500")
            .build())
        ).buyer(Buyer.builder()
            .email(checkoutForm.getEmail())
            .language("pl")
            .build()
        ).build();
  }
}
