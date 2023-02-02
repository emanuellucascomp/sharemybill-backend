package br.com.embole.sharemybill.bill.application.service;

import br.com.embole.sharemybill.bill.adapter.out.persistence.BillRepository;
import br.com.embole.sharemybill.bill.application.port.in.BillingUseCase;
import br.com.embole.sharemybill.bill.application.port.in.request.BillPerCustomerRequest;
import br.com.embole.sharemybill.bill.application.port.in.request.BillingRequest;
import br.com.embole.sharemybill.bill.application.port.in.request.ProductRequest;
import br.com.embole.sharemybill.bill.application.port.in.response.BillingResponse;
import br.com.embole.sharemybill.bill.application.port.out.PaymentRequestService;
import br.com.embole.sharemybill.bill.application.port.out.response.PaymentResponse;
import br.com.embole.sharemybill.bill.domain.Bill;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    @Mock
    private  PaymentRequestService paymentRequestService;
    @Mock
    private BillRepository billRepository;
    @InjectMocks
    private BillingService billingService;

    @Test
    @DisplayName("Deve calcular com sucesso o valor total a pagar de cada pessoa")
    void shouldCalculateBillingWithSuccess(){
        BillingRequest request = BillingRequest.builder()
                .deliveryAmount(BigDecimal.valueOf(8))
                .discountAmount(BigDecimal.valueOf(20))
                .billsPerCustomer(List.of(
                        BillPerCustomerRequest.builder()
                                .clientDocument("12345678900")
                                .consumedProducts(List.of(
                                        ProductRequest.builder()
                                                .productName("Hamburguer")
                                                .productPrice(BigDecimal.valueOf(40))
                                                .build(),
                                        ProductRequest.builder()
                                                .productName("Sobremesa")
                                                .productPrice(BigDecimal.valueOf(2))
                                                .build()
                                ))
                                .build(),
                        BillPerCustomerRequest.builder()
                                .clientDocument("12345678911")
                                .consumedProducts(List.of(
                                        ProductRequest.builder()
                                                .productName("Sanduíche")
                                                .productPrice(BigDecimal.valueOf(8))
                                                .build()
                                ))
                                .build()
                ))
                .build();
        Bill bill = new Bill(UUID.randomUUID(), "12345678911", BigDecimal.valueOf(40), BigDecimal.valueOf(40), BigDecimal.valueOf(20), null);

        when(paymentRequestService.requestPaymentForClient(any())).thenReturn(PaymentResponse.builder()
                        .paymentUrl("https://payment.url.com.br")
                .build());
        when(billRepository.save(any())).thenReturn(bill);

        BillingResponse billingResponse = billingService.calculateBilling(request);

        assertNotNull(billingResponse);
        verify(billRepository, times(2)).save(any());
        verify(paymentRequestService, times(2)).requestPaymentForClient(any());
        assertEquals(BigDecimal.valueOf(31.92).setScale(2, RoundingMode.HALF_UP), billingResponse.getBillsPerCustomer().get(0).getAmountToPay());
        assertEquals(BigDecimal.valueOf(16.80).setScale(2, RoundingMode.HALF_UP), billingResponse.getBillsPerCustomer().get(0).getTotalDiscount());
        assertEquals("https://payment.url.com.br", billingResponse.getBillsPerCustomer().get(0).getPaymentUrl());
        assertEquals(BigDecimal.valueOf(6.08).setScale(2, RoundingMode.HALF_UP), billingResponse.getBillsPerCustomer().get(1).getAmountToPay());
        assertEquals(BigDecimal.valueOf(3.20).setScale(2, RoundingMode.HALF_UP), billingResponse.getBillsPerCustomer().get(1).getTotalDiscount());
    }
}