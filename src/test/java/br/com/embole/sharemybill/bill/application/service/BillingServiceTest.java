package br.com.embole.sharemybill.bill.application.service;

import br.com.embole.sharemybill.bill.application.port.in.BillingUseCase;
import br.com.embole.sharemybill.bill.application.port.in.request.BillPerCustomerRequest;
import br.com.embole.sharemybill.bill.application.port.in.request.BillingRequest;
import br.com.embole.sharemybill.bill.application.port.in.request.ProductRequest;
import br.com.embole.sharemybill.bill.application.port.in.response.BillingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

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

        BillingResponse billingResponse = billingService.calculateBilling(request);

        assertNotNull(billingResponse);
        assertEquals(BigDecimal.valueOf(31.92).setScale(2, RoundingMode.HALF_UP), billingResponse.getBillsPerCustomer().get(0).getAmountToPay());
        assertEquals(BigDecimal.valueOf(16.80).setScale(2, RoundingMode.HALF_UP), billingResponse.getBillsPerCustomer().get(0).getTotalDiscount());
        assertEquals(BigDecimal.valueOf(6.08).setScale(2, RoundingMode.HALF_UP), billingResponse.getBillsPerCustomer().get(1).getAmountToPay());
        assertEquals(BigDecimal.valueOf(3.20).setScale(2, RoundingMode.HALF_UP), billingResponse.getBillsPerCustomer().get(1).getTotalDiscount());
    }
}