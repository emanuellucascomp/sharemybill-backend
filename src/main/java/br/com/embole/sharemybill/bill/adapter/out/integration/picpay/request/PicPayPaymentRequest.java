package br.com.embole.sharemybill.bill.adapter.out.integration.picpay.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PicPayPaymentRequest {

    private String referenceId;
    private String callbackUrl;
    private BigDecimal value;
    private PicPayBuyerRequest buyer;
}
