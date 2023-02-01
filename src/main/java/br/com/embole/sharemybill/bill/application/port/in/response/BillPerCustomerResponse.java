package br.com.embole.sharemybill.bill.application.port.in.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillPerCustomerResponse {
    private String clientDocument;
    private BigDecimal originalAmount;
    private BigDecimal amountToPay;
    private BigDecimal totalDiscount;
    private String paymentUrl;
}
