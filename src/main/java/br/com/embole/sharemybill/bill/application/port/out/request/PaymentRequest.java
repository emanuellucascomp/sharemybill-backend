package br.com.embole.sharemybill.bill.application.port.out.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String referenceId;
    private String callbackUrl;
    private BigDecimal value;
    private BuyerRequest buyer;
}
