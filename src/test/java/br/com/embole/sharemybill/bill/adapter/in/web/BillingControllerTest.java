package br.com.embole.sharemybill.bill.adapter.in.web;

import br.com.embole.sharemybill.bill.application.port.in.BillingUseCase;
import br.com.embole.sharemybill.bill.application.port.in.request.BillPerCustomerRequest;
import br.com.embole.sharemybill.bill.application.port.in.request.BillingRequest;
import br.com.embole.sharemybill.bill.application.port.in.request.ProductRequest;
import br.com.embole.sharemybill.bill.application.port.in.response.BillPerCustomerResponse;
import br.com.embole.sharemybill.bill.application.port.in.response.BillingResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import static org.hamcrest.CoreMatchers.is;

import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = BillingController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class BillingControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BillingUseCase billingUseCase;

    @Test
    void shouldCalculateBilling() throws Exception {
        BillingResponse response = BillingResponse.builder()
                .billsPerCustomer(List.of(BillPerCustomerResponse.builder()
                                .amountToPay(BigDecimal.valueOf(100))
                                .clientDocument("12345678911")
                                .originalAmount(BigDecimal.valueOf(110))
                                .totalDiscount(BigDecimal.valueOf(10))
                                .paymentUrl("http://picpay.com.br/payment")
                        .build()))
                .build();
        BillingRequest request = BillingRequest.builder()
                .deliveryAmount(BigDecimal.valueOf(110))
                .discountAmount(BigDecimal.valueOf(10))
                .billsPerCustomer(List.of(
                        BillPerCustomerRequest.builder()
                                .clientDocument("12345678911")
                                .consumedProducts(List.of(ProductRequest.builder()
                                                .productName("Bife de tira")
                                                .productPrice(BigDecimal.valueOf(110))
                                        .build()))
                                .build()
                ))
                .build();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(request);

        given(billingUseCase.calculateBilling(Mockito.any())).willReturn(response);

        mvc.perform(post("/ap1/v1/billing").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
        verify(billingUseCase, VerificationModeFactory.times(1)).calculateBilling(Mockito.any());
    }
}