package com.createam.controller;

import com.createam.PayUForm;
import com.createam.config.PayUConfigurationProperties;
import com.createam.payu.order.PayUOrderService;
import com.createam.payu.order.model.Buyer;
import com.createam.payu.order.model.OrderCreateRequest;
import com.createam.payu.order.model.OrderCreateResponse;
import com.createam.payu.order.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static com.createam.payu.order.model.OrderCreateResponse.Status.STATUS_CODE_SUCCESS;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PayUController {

    public static final String URL_PAYU_DEMO_INDEX = "/";
    public static final String URL_PAYMENT_CALLBACK = "/payu-callback";
    public static final String TEMPLATE_PAYU_FORM = "payu-form";

    private final PayUOrderService orderService;
    private final PayUConfigurationProperties payUConfiguration;

    @GetMapping(URL_PAYU_DEMO_INDEX)
    public String payuForm(Model model, HttpServletRequest request) {
        model.addAttribute("payuForm", new PayUForm());

        request.getSession().getAttribute("rawResponse");

        return TEMPLATE_PAYU_FORM;
    }

    @PostMapping(URL_PAYU_DEMO_INDEX)
    public RedirectView handleCheckout(
            @ModelAttribute("payuForm") PayUForm payUForm,
            HttpServletRequest request,
            Model model) {
        log.info(payUForm.toString());

        final OrderCreateRequest orderRequest = prepareOrderCreateRequest(payUForm, request);

        log.info("Order request = {}", orderRequest);

        final OrderCreateResponse orderResponse = orderService.order(orderRequest);

        if (!orderResponse.getStatus().getStatusCode().equals(STATUS_CODE_SUCCESS)) {
            throw new RuntimeException("Payment failed! ");
        }

        return new RedirectView(orderResponse.getRedirectUri());
    }

    @GetMapping(URL_PAYMENT_CALLBACK)
    public String handlePaymentCallback(final @RequestParam Optional<String> error, Model model, HttpServletRequest request) {
        model.addAttribute("hasError", error.isPresent());
        model.addAttribute("paymentFinished", true);

        error.ifPresent(e -> model.addAttribute("paymentErrorCode", e));

        return payuForm(model, request);
    }

    private OrderCreateRequest prepareOrderCreateRequest(final PayUForm payUForm, final HttpServletRequest request) {
        return OrderCreateRequest.builder()
                .customerIp(request.getRemoteAddr())
                .merchantPosId(payUConfiguration.getMerchantPosId())
                .description(payUConfiguration.getDescription())
                .currencyCode("PLN")
                .totalAmount(
                        Objects.nonNull(payUForm.getProductPrice())
                                ? String.valueOf(100 * payUForm.getProductPrice())
                                : "2500"
                ).products(
                        Collections.singletonList(
                                Product.builder()
                                        .name(
                                                StringUtils.isNotBlank(payUForm.getProductName())
                                                        ? payUForm.getProductName()
                                                        : "Test product name"
                                        ).quantity("1")
                                        .unitPrice(
                                                Objects.nonNull(payUForm.getProductPrice())
                                                        ? String.valueOf(100 * payUForm.getProductPrice())
                                                        : "2500"
                                        ).build()
                        )).buyer(
                        Buyer.builder()
                                .email(payUForm.getEmail())
                                .language("pl")
                                .build()
                ).build();
    }
}
