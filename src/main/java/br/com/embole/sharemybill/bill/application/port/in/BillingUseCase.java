package br.com.embole.sharemybill.bill.application.port.in;

import br.com.embole.sharemybill.bill.application.port.in.request.BillingRequest;
import br.com.embole.sharemybill.bill.application.port.in.response.BillingResponse;

public interface BillingUseCase {
    BillingResponse calculateBilling(BillingRequest request);
}
