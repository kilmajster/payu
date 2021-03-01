package com.createam.controller;

import static com.createam.payu.order.model.OrderCreateResponse.Status.STATUS_CODE_SUCCESS;
import static java.util.Arrays.asList;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
public class PayUController {

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
  public PayUController(final OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping(URL_INDEX)
  public String index(final Model model) {
    model.addAttribute(new CheckoutForm());
    return PAGE_INDEX;
  }

  @PostMapping(URL_CHECKOUT)
  public RedirectView checkout(final @ModelAttribute CheckoutForm checkoutForm, final HttpServletRequest request) throws UnknownHostException {
    final OrderCreateRequest orderRequest = prepareOrderCreateRequest(checkoutForm, request);

    log.info("orderRequest = {}", orderRequest);

    final OrderCreateResponse response = orderService.order(orderRequest);

    if (!response.getStatus().getStatusCode().equals(STATUS_CODE_SUCCESS)) {
      throw new RuntimeException("Payment failed! ");
    }

    log.info("response.getRedirectUri() = {}", response.getRedirectUri());
    log.info("response.getOrderId() = {}", response.getOrderId());
    log.info("response.getStatus() = {}", response.getStatus());
    log.info("response.toString() = {}", response.toString());

    log.info("InetAddress.getLocalHost().getHostAddress() = {}", InetAddress.getLocalHost().getHostAddress());
    log.info("InetAddress.getLocalHost().getHostName() = {}", InetAddress.getLocalHost().getHostName());
    log.info("InetAddress.getLoopbackAddress().getHostAddress() = {}", InetAddress.getLoopbackAddress().getHostAddress());
    log.info("InetAddress.getLoopbackAddress().getHostName() = {}", InetAddress.getLoopbackAddress().getHostName());

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
