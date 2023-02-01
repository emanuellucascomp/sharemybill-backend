package br.com.embole.sharemybill.bill.application.port.out;

import br.com.embole.sharemybill.bill.application.port.out.request.PaymentRequest;
import br.com.embole.sharemybill.bill.application.port.out.response.PaymentResponse;

public interface PaymentRequestService {

    PaymentResponse requestPaymentForClient(PaymentRequest request);
}
