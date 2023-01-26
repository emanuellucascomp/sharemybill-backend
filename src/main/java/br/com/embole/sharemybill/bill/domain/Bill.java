package br.com.embole.sharemybill.bill.domain;

import java.math.BigDecimal;

public class Bill {
    private String clientDocument;
    private BigDecimal originalAmount;
    private BigDecimal amountToPay;
    private BigDecimal totalDiscount;
}
