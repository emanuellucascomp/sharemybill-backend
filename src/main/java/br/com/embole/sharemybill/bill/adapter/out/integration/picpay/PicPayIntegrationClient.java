package br.com.embole.sharemybill.bill.adapter.out.integration.picpay;

import br.com.embole.sharemybill.bill.adapter.out.integration.picpay.request.PicPayPaymentRequest;
import br.com.embole.sharemybill.bill.adapter.out.integration.picpay.response.PicPayPaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "pic-pay-integration", url = "${pic-pay.base-url}")
public interface PicPayIntegrationClient {

    @PostMapping
    PicPayPaymentResponse addPaymentToContract(@RequestHeader("x-picpay-token") String token,
                                               PicPayPaymentRequest request);
}
