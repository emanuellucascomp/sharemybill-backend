package br.com.embole.sharemybill.bill.adapter.out.integration.picpay;

import br.com.embole.sharemybill.bill.adapter.out.integration.picpay.request.PicPayBuyerRequest;
import br.com.embole.sharemybill.bill.adapter.out.integration.picpay.request.PicPayPaymentRequest;
import br.com.embole.sharemybill.bill.adapter.out.integration.picpay.response.PicPayPaymentResponse;
import br.com.embole.sharemybill.bill.application.port.out.PaymentRequestService;
import br.com.embole.sharemybill.bill.application.port.out.request.PaymentRequest;
import br.com.embole.sharemybill.bill.application.port.out.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PicPayPaymentAdapter implements PaymentRequestService {

    private final PicPayIntegrationClient picPayIntegrationClient;
    @Value("${pic-pay.apiToken}")
    private String apiToken;

    @Override
    public PaymentResponse requestPaymentForClient(PaymentRequest request) {
        PicPayPaymentResponse picPayPaymentResponse = picPayIntegrationClient.addPaymentToContract(apiToken,
                PicPayPaymentRequest.builder()
                        .value(request.getValue())
                        .referenceId(request.getReferenceId())
                        .callbackUrl(request.getCallbackUrl())
                        .buyer(PicPayBuyerRequest.builder()
                                .document(request.getBuyer().getDocument())
                                .build())
                        .build());
        return PaymentResponse.builder()
                .paymentUrl(picPayPaymentResponse.getPaymentUrl())
                .build();
    }
}
