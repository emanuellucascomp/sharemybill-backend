package br.com.embole.sharemybill.bill.adapter.out.integration.picpay;

import br.com.embole.sharemybill.bill.adapter.out.integration.picpay.response.PicPayPaymentResponse;
import br.com.embole.sharemybill.bill.application.port.out.request.BuyerRequest;
import br.com.embole.sharemybill.bill.application.port.out.request.PaymentRequest;
import br.com.embole.sharemybill.bill.application.port.out.response.PaymentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PicPayPaymentAdapterTest {

    @Mock
    private PicPayIntegrationClient picPayIntegrationClient;

    @InjectMocks
    private PicPayPaymentAdapter picPayPaymentAdapter;

    @Test
    @DisplayName("Deve retornar a integração com o PicPay com sucesso")
    void shouldReturnPaymentResponseWithSuccess(){
        PaymentRequest request = PaymentRequest.builder()
                .value(BigDecimal.valueOf(100))
                .referenceId(UUID.randomUUID().toString())
                .callbackUrl("http://callbackurl.com.br")
                .buyer(BuyerRequest.builder()
                        .document("12345678911")
                        .build())
                .build();
        PicPayPaymentResponse response = PicPayPaymentResponse.builder()
                .paymentUrl("http://picpay.com.br/payment")
                .referenceId(UUID.randomUUID().toString())
                .build();

        when(picPayIntegrationClient.addPaymentToContract(any(), any())).thenReturn(response);

        PaymentResponse paymentResponse = picPayPaymentAdapter.requestPaymentForClient(request);

        assertNotNull(paymentResponse);
        assertEquals("http://picpay.com.br/payment", paymentResponse.getPaymentUrl());
    }

}